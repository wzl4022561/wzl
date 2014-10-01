package nxt;

import nxt.crypto.Crypto;
import nxt.util.Logger;
import nxt.util.Time;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.util.Properties;

public abstract class BlockchainTest extends AbstractBlockchainTest {

    protected static int baseHeight;

    protected static final String forgerSecretPhrase = "aSykrgKGZNlSVOMDxkZZgbTvQqJPGtsBggb";
    protected static final String secretPhrase1 = "hope peace happen touch easy pretend worthless talk them indeed wheel state";
    protected static final String secretPhrase2 = "rshw9abtpsa2";

    protected static long id1;
    protected static long id2;

    @Before
    public void init() {
        id1 = Account.getAccount(Crypto.getPublicKey(secretPhrase1)).getId();
        id2 = Account.getAccount(Crypto.getPublicKey(secretPhrase2)).getId();

        Properties properties = ManualForgingTest.newTestProperties();
        properties.setProperty("nxt.isTestnet", "true");
        properties.setProperty("nxt.isOffline", "true");
        properties.setProperty("nxt.enableFakeForging", "true");
        properties.setProperty("nxt.timeMultiplier", "1");
        AbstractForgingTest.init(properties);
        Assert.assertTrue("nxt.fakeForgingAccount must be defined in nxt.properties", Nxt.getStringProperty("nxt.fakeForgingAccount") != null);
        Nxt.setTime(new Time.CounterTime(Nxt.getEpochTime()));
        baseHeight = blockchain.getHeight();
        Logger.logMessage("baseHeight: " + baseHeight);
    }

    @After
    public void destroy() {
        AbstractForgingTest.shutdown();
    }

    public static void generateBlock() {
        try {
            blockchainProcessor.generateBlock(forgerSecretPhrase, Nxt.getEpochTime());
        } catch (BlockchainProcessor.BlockNotAcceptedException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

}
