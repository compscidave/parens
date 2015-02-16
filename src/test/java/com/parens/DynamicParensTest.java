package com.parens;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigInteger;

import org.junit.Ignore;
import org.junit.Test;

public class DynamicParensTest {

    private static Parens parens;

    @Test
    public void testValidExpressionOne() {
        parens = new Parens("1a1");
        assertTrue(true);
    }

    @Test
    public void testValidExpressionTwo() {
        parens = new Parens("1r1x1n0a0");
        assertTrue(parens.calculate().equals(BigInteger.valueOf(8)));
        /*
         * 1r(1x(1n(0a0))) = 1 1r(1x((1n0)a0)) = 1 1r((1x1)n(0a0)) = 1
         * 1r((1x(1n0))a0) = 1 1r(((1x1)n0)a0) = 1 (1r1)x((1n0)a0) = 1
         * (1r(1x1))n(0a0) = 1 ((1r1)x1)n(0a0) = 1
         * 
         * All other forms of the expression evaluate to 0 (false). Note: there
         * are six such expressions.
         * 
         * (1r1)x(1n(0a0)) = 0 (1r(1x(1n0)))a0 = 0 (1r((1x1)n0))a0 = 0
         * ((1r1)x(1n0))a0 = 0 ((1r(1x1))n0)a0 = 0 (((1r1)x1)n0)a0 = 0
         */
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidExpression1() {
        parens = new Parens("1a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidExpression2() {
        parens = new Parens("1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidExpression3() {
        parens = new Parens("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidExpression4() {
        parens = new Parens("a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidExpression5() {
        parens = new Parens("a1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidExpression6() {
        parens = new Parens("11");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidExpression7() {
        parens = new Parens(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidExpression8() {
        parens = new Parens("aa");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidExpression9() {
        parens = new Parens("1t1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidExpressionTen() {
        parens = new Parens("1a2");
    }

    @Test
    public void testOperatorAND() {
        parens = new Parens("1a1");
        assertTrue(parens.calculate().equals(BigInteger.ONE));
        parens = new Parens("1a0");
        assertTrue(parens.calculate().equals(BigInteger.ZERO));
        parens = new Parens("0a1");
        assertTrue(parens.calculate().equals(BigInteger.ZERO));
        parens = new Parens("0a0");
        assertTrue(parens.calculate().equals(BigInteger.ZERO));
    }

    @Test
    public void testOperatorOR() {
        parens = new Parens("1r1");
        assertTrue(parens.calculate().equals(BigInteger.ONE));
        parens = new Parens("1r0");
        assertTrue(parens.calculate().equals(BigInteger.ONE));
        parens = new Parens("0r1");
        assertTrue(parens.calculate().equals(BigInteger.ONE));
        parens = new Parens("0r0");
        assertTrue(parens.calculate().equals(BigInteger.ZERO));
    }

    @Test
    public void testOperatorXOR() {
        parens = new Parens("1x1");
        assertTrue(parens.calculate().equals(BigInteger.ZERO));
        parens = new Parens("1x0");
        assertTrue(parens.calculate().equals(BigInteger.ONE));
        parens = new Parens("0x1");
        assertTrue(parens.calculate().equals(BigInteger.ONE));
        parens = new Parens("0x0");
        assertTrue(parens.calculate().equals(BigInteger.ZERO));
    }

    @Test
    public void testOperatorNAND() {
        parens = new Parens("1n1");
        assertTrue(parens.calculate().equals(BigInteger.ZERO));
        parens = new Parens("1n0");
        assertTrue(parens.calculate().equals(BigInteger.ONE));
        parens = new Parens("0n1");
        assertTrue(parens.calculate().equals(BigInteger.ONE));
        parens = new Parens("0n0");
        assertTrue(parens.calculate().equals(BigInteger.ONE));
    }

    @Test
    public void testCatalanOneHundred() {
        String inputString = "1";
        for (int i = 1; i <= 100; i++) {
            inputString += "a1";
        }
        parens = new Parens(inputString);
        BigInteger truths = parens.calculate();
        /*
         * 200! / (101! * 100!)
         */
        assertTrue(truths.equals(new BigInteger(
                "896519947090131496687170070074100632420837521538745909320")));
    }

    /**
     * Remove the ignore at your own risk. On a moderately fast quad-core
     * desktop running a 32-bit IDE with maxed ram, the following test takes
     * roughly 150-180 seconds to finish. Forcing 64-bit mode through command
     * line or in a 64-bit IDE reduces runtime marginally.
     * 
     */
    @Ignore
    @Test
    public void testCatalanOneThousand() {

        String inputString = "1";

        for (int i = 1; i <= 1000; i++) {
            inputString += "a1";
        }
        parens = new Parens(inputString);
        BigInteger truths = parens.calculate();
        /*
         * the 1000th catalan 2000! / (1001! * 1000!) 7.25min | ~435sec
         */
        assertTrue(truths
                .equals(new BigInteger(
                        "2046105521468021692642519982997827217179245642339057975844538099572176010191891863964968026156453752"
                                + "4490157505694285950973181636343701546373806668828863752033596532433909297174310804435090075047729129"
                                + "7314225320935212694683984479674769763853760010063791881932656973098208302153805708771117628577790927"
                                + "5869648636874856805956580057673173655666887003493944650164153396910927037406301799052584663611016897"
                                + "2728933055321162921432710371407187516258398120726824643431537929562817485824357514814985980875869986"
                                + "03921577523657477775758899987954012641033870640665444651660246024318184109046864244732001962029120")));

    }

    @Ignore
    @Test
    public void testCatalanOneThousandMixed() {

        String inputString = "1";

        for (int i = 1; i <= 1000; i++) {
            if (i % 2 == 1) {
                inputString += "a1";
            } else {
                inputString += "n0";
            }
        }
        parens = new Parens(inputString);
        BigInteger truths = parens.calculate();
        /*
         * roughly 69.46% of the 1000th Catalan. (2000! / (1001! * 1000!)) *
         * 0.6946 = ~ the number of true parenthesizations of
         * 1a1n0a1n0a...0a1n0a
         */
        assertTrue(truths
                .equals(new BigInteger(
                        "1421185274468560843305167906938290972358115375984645975247433382148829038506213876539251638711215913"
                                + "9286275039299386596912123497935804162752260437126676755343890699475098983646195612998420718243205797"
                                + "4587124876423719852510981321872081601010617450661124273163891029547232533041156271349327154365753973"
                                + "5523867586185986858122272017390327235683873410032462984528545858800306795707109901023343253607697052"
                                + "9107101826654048708797243040089836615873545129026861646131626199717208279071903700050967517863466394"
                                + "23119341947429217171822649983119946157251593122932910744565145916236775783333398863354053796342160")));

    }

    @Ignore
    @Test
    public void testCatalanTwoThousand() {

        String inputString = "1";

        for (int i = 1; i <= 2000; i++) {
            inputString += "a1";
        }
        parens = new Parens(inputString);
        BigInteger truths = parens.calculate();
        /*
         * 4000! / (2001! * 2000!) ~1500000ms | 25min
         */
        assertTrue(truths
                .equals(new BigInteger(
                        "8310334208065142776309529367618098740710772600642880285497172797668390892100214567591803395202640482"
                                + "8172947462148184140092293772615069543749919579401913184542190789998131157935053609513254653307083479"
                                + "0227489302482829125100567918938377462105826094373770187950030383359149306717316457566717966958654007"
                                + "2380712678409760792703474414242765784871724291694937904834015667075011782054107910351085129485282437"
                                + "5417656282802051502894290423897489394895646374720720500469015112081379334417342812570507972243484674"
                                + "0173128707227696585533551197635988680908303537863772991575595714830611217016162275141082663737386119"
                                + "6388563955715620579274552030664438610113953593262863178924036818352384793166626985408084019051204416"
                                + "8904920315140097986474066384787791689172949296246077785401782902221715014320983995273869460977894248"
                                + "5711269847560760415764555065413654067931684397776240990537761972213304506205892470126785741371117041"
                                + "7196970798565170035697524680103780970820492802926688067380928318938551365171103404275165312933041098"
                                + "8429748890822794852876294980940940475489863815497115328496368962420559731379861218561596056806164119"
                                + "183850202316884943615615364399614171566659303464840432312167886200724171888198781484458773618136640")));

    }

    @Ignore
    @Test
    public void testCatalanTwoThousandFiveHundred() {

        String inputString = "1";

        for (int i = 1; i <= 2500; i++) {
            inputString += "a1";
        }
        parens = new Parens(inputString);
        BigInteger truths = parens.calculate();
        /*
         * the 2500th Catalan. (5000! / (2501! * 2500!)) 7783162ms | ~2h9m
         */
        assertTrue(truths
                .equals(new BigInteger(
                        "6372325811073324173094865225653414983035303615228827423076456972111890196918605232421916521340951948"
                                + "0876653899462560073247878949956163452771813676347687235870846586258053569529546764678700852556949187"
                                + "3709975580106879168427203395939388008928916651066837220802520029442006440443458360118666155032486039"
                                + "3448301921013489332033468612724600544605441130002282307017801209500443011918328243286078726309428680"
                                + "6437538943056211188415485508070990063347671773915988005312318023950388604433068179084334212486148835"
                                + "3182598029310714422004124001014749723971907083072078028062436447038158949227251155443023609134022195"
                                + "3833155143650843562935859361746227885238798166454114251505676480181028528618994725146908028270628152"
                                + "5196473641203052407592868540468074461542381199319018383974672490708512571989814966610218508975984521"
                                + "1489587213728693438419567609989641630756456121646480147621855441662438868082646685311287932711068971"
                                + "2019002683409383781649748979331643248580501339792279862244445524984831061570567360102937264139255313"
                                + "3050228073973345770891289824902895565462520526296395575911170651046985003611550888482382534586486618"
                                + "7428554708303266945775772569463194592885574356041992352237762517345945136161929916657407538019415469"
                                + "1590271023485902900959067805218847887151632693961875387764552894868112253545683751207975128770291419"
                                + "9653919834809496874969514059034928265648104264306809580718328419418920052250994671632095475125496309"
                                + "4831183677646245242650458819337600800010903760047699048081828326514477425505277747170597044092916320")));

    }

}
