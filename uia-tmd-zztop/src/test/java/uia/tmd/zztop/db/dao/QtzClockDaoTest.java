package uia.tmd.zztop.db.dao;

import org.junit.Test;

import uia.tmd.zztop.db.QtzClock;

import ui.tmd.zztop.DB;
import ui.tmd.zztop.ZztopEnv;

public class QtzClockDaoTest {

    @Test
    public void testInsert() throws Exception {
        ZztopEnv.initial();

        QtzClockDao dao = new QtzClockDao(DB.create());

        QtzClock qc = new QtzClock();
        qc.setTmdJobBo("SHOP_ORDER");
        qc.setClockType("min");
        dao.insert(qc);
    }
}
