package com.stkj.cashier.greendao.generate;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.stkj.cashier.bean.db.CompanyMemberdbEntity;
import com.stkj.cashier.cbgfacepass.model.FacePassPeopleInfo;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig facePassPeopleInfoDaoConfig;
    private final DaoConfig companyMemberdbEntityDaoConfig;

    private final FacePassPeopleInfoDao facePassPeopleInfoDao;
    private final CompanyMemberdbEntityDao companyMemberdbEntityDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        facePassPeopleInfoDaoConfig = daoConfigMap.get(FacePassPeopleInfoDao.class).clone();
        facePassPeopleInfoDaoConfig.initIdentityScope(type);

        companyMemberdbEntityDaoConfig = daoConfigMap.get(CompanyMemberdbEntityDao.class).clone();
        companyMemberdbEntityDaoConfig.initIdentityScope(type);

        facePassPeopleInfoDao = new FacePassPeopleInfoDao(facePassPeopleInfoDaoConfig, this);
        companyMemberdbEntityDao = new CompanyMemberdbEntityDao(companyMemberdbEntityDaoConfig, this);

        registerDao(FacePassPeopleInfo.class, facePassPeopleInfoDao);
        registerDao(CompanyMemberdbEntity.class, companyMemberdbEntityDao);
    }
    
    public void clear() {
        facePassPeopleInfoDaoConfig.clearIdentityScope();
        companyMemberdbEntityDaoConfig.clearIdentityScope();
    }

    public FacePassPeopleInfoDao getFacePassPeopleInfoDao() {
        return facePassPeopleInfoDao;
    }

    public CompanyMemberdbEntityDao getCompanyMemberdbEntityDao() {
        return companyMemberdbEntityDao;
    }

}
