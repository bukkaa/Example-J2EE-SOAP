package ws.main.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ftc.cs.acq.data.AcqException;
import ru.ftc.cs.cs20.utils.support.TxSupport;
import ru.ftc.cs.mpics.ws.db.operative.MpicsRequestRepository;
import ru.ftc.cs.mpics.ws.db.operative.model.MpicsRequest;
import ru.ftc.cs.mpics.ws.web.MpiCsInternalResultCode;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;

import static ru.ftc.cs.acq.util.PersistenceExceptionUtil.isConstraintViolated;

@ApplicationScoped
public class RequestService {
    private static final Logger LOG = LoggerFactory.getLogger(RequestService.class);

    @Inject
    private MpicsRequestRepository mpicsRequestRepository;

    @Inject
    private TxSupport transaction;

    /**
     * Persists the request.
     * If request identifier is duplicated then request with {@code null} SUK will be
     * persisted and {@link AcqException} will be thrown.
     *
     * @param mpicsRequest the request to persist.
     *
     * @throws AcqException if request order Id is duplicated for request's merchant.
     */
    public void persist(@Nonnull MpicsRequest mpicsRequest) {
        LOG.debug("persist request <-- ");
        try {
            mpicsRequest.setSuk(
                    String.format(
                            "%s_%s_%s_%s",
                            MpicsRequest.MPICS_WS_INTERFACE_NAME,
                            mpicsRequest.getRequestOriginator(),
                            mpicsRequest.getRequestInstance(),
                            mpicsRequest.getRequestId()));
            transaction.requiresNew(() -> mpicsRequestRepository.persistAndFlush(mpicsRequest));
            LOG.debug("persist request -->");
        } catch (PersistenceException e) {
            LOG.debug("unable to persist original request", e);
            if (isConstraintViolated(e)) {
                // to save result in DB it is necessary to ...
                // 1) set 'Surrogate Unique Identifier' to null
                mpicsRequest.setSuk(null);
                // 2) clear auto-generated identifiers (to prevent 'detached entity passed to persist' error)
                mpicsRequest.setId(0L);
                mpicsRequest.resetRequestPaResIfExist();
                mpicsRequest.getExtensions().forEach(extension -> {
                    extension.setId(0L);
                    extension.getParams().forEach(param -> param.setId(0L));
                });
                transaction.requiresNew(() -> mpicsRequestRepository.persistAndFlush(mpicsRequest));
                throw new AcqException(
                        MpiCsInternalResultCode.DUPLICATE_REQUEST_ERROR,
                        "Duplicate of request identifier was received: " +
                                ((mpicsRequest.getRequestInstance() != null) ?
                                        "instance='"+mpicsRequest.getRequestInstance()+"', id=" :
                                        "") +
                                mpicsRequest.getRequestId());
            } else {
                throw e;
            }
        }
    }

    /**
     * Updates response fields of request and order identifier.
     *
     * @param mpicsRequest the request to update. It should contain non-null {@code id}.
     */
    public void update(@Nonnull MpicsRequest mpicsRequest) {
        transaction.requiresNew(() -> mpicsRequestRepository.update(mpicsRequest));
    }

    public void checkRequestAmount(long requestAmount) {
        if (requestAmount <= 0L) {
            throw new AcqException(MpiCsInternalResultCode.INVALID_AMOUNT, "Invalid amount: " + requestAmount);
        }
    }
}
