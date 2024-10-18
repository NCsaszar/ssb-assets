package com.smoothstack.branchservice.model.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class CustomBranchIdGenerator implements IdentifierGenerator {

    private static int counter = 0;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String prefix = "SSB-";
        return prefix + String.format("%03d", ++counter);
    }
}