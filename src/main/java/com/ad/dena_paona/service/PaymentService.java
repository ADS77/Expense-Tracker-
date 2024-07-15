package com.ad.dena_paona.service;

import com.ad.dena_paona.entity.Payment;
import com.ad.dena_paona.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public List<Payment> getPaymentByLoanId(Long loanId){
        return paymentRepository.findByLoanId(loanId);
    }

    public Payment getPaymentByPaymentId(Long id){
        return paymentRepository.findById(id).orElse(null );
    }

    public Payment updatePayment (Long id, Payment paymentDetails){
        Payment payment = getPaymentByPaymentId(id);
        if(payment != null){
            payment.setAmount(paymentDetails.getAmount());
            payment.setPaymentDate(paymentDetails.getPaymentDate());
            return paymentRepository.save(payment);
        }
        return null;
    }

    public Payment createPayment(Payment payment){
        return paymentRepository.save(payment);
    }

    public void deletePayment(Payment payment){
        paymentRepository.delete(payment);
    }

    public void deletePayment(Long id){
        paymentRepository.deleteById(id);
    }
}
