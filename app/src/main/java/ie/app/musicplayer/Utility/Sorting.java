package ie.app.musicplayer.Utility;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;

public class Sorting {

    private HashMap<Character, String> codeVN = new HashMap<>();

    public Sorting() {
        createCode();
    }

    private void createCode() {
        codeVN.put(' ', "000");
        codeVN.put('!', "001");
        codeVN.put('\"', "002");
        codeVN.put('#', "003");
        codeVN.put('$', "004");
        codeVN.put('%', "005");
        codeVN.put('&', "006");
        codeVN.put('\'', "007");
        codeVN.put('(', "008");
        codeVN.put(')', "009");
        codeVN.put('*', "010");
        codeVN.put('+', "011");
        codeVN.put(',', "012");
        codeVN.put('-', "013");
        codeVN.put('.', "014");
        codeVN.put('/', "015");
        codeVN.put('0', "016");
        codeVN.put('1', "017");
        codeVN.put('2', "018");
        codeVN.put('3', "019");
        codeVN.put('4', "020");
        codeVN.put('5', "021");
        codeVN.put('6', "022");
        codeVN.put('7', "023");
        codeVN.put('8', "024");
        codeVN.put('9', "025");
        codeVN.put(':', "026");
        codeVN.put(';', "027");
        codeVN.put('<', "028");
        codeVN.put('=', "029");
        codeVN.put('>', "030");
        codeVN.put('?', "031");
        codeVN.put('@', "032");
        codeVN.put('[', "033");
        codeVN.put('\\', "034");
        codeVN.put(']', "035");
        codeVN.put('^', "036");
        codeVN.put('_', "037");
        codeVN.put('`', "038");

        codeVN.put('a', "039");
        codeVN.put('á', "040");
        codeVN.put('à', "041");
        codeVN.put('ả', "042");
        codeVN.put('ã', "043");
        codeVN.put('ạ', "044");
        codeVN.put('ă', "045");
        codeVN.put('ắ', "046");
        codeVN.put('ằ', "047");
        codeVN.put('ẳ', "048");
        codeVN.put('ẵ', "049");
        codeVN.put('ặ', "050");
        codeVN.put('â', "051");
        codeVN.put('ấ', "052");
        codeVN.put('ầ', "053");
        codeVN.put('ẩ', "054");
        codeVN.put('ẫ', "055");
        codeVN.put('ậ', "056");
        codeVN.put('b', "057");
        codeVN.put('c', "058");
        codeVN.put('d', "059");
        codeVN.put('đ', "060");
        codeVN.put('e', "061");
        codeVN.put('é', "062");
        codeVN.put('è', "063");
        codeVN.put('ẻ', "064");
        codeVN.put('ẽ', "065");
        codeVN.put('ẹ', "066");
        codeVN.put('ê', "067");
        codeVN.put('ế', "068");
        codeVN.put('ề', "069");
        codeVN.put('ể', "070");
        codeVN.put('ễ', "071");
        codeVN.put('ệ', "072");
        codeVN.put('f', "073");
        codeVN.put('g', "074");
        codeVN.put('h', "075");
        codeVN.put('i', "076");
        codeVN.put('í', "077");
        codeVN.put('ì', "078");
        codeVN.put('ỉ', "079");
        codeVN.put('ĩ', "080");
        codeVN.put('ị', "081");
        codeVN.put('j', "082");
        codeVN.put('k', "083");
        codeVN.put('l', "084");
        codeVN.put('m', "085");
        codeVN.put('n', "086");
        codeVN.put('o', "087");
        codeVN.put('ó', "088");
        codeVN.put('ò', "089");
        codeVN.put('ỏ', "090");
        codeVN.put('õ', "091");
        codeVN.put('ọ', "092");
        codeVN.put('ơ', "093");
        codeVN.put('ớ', "094");
        codeVN.put('ờ', "095");
        codeVN.put('ở', "096");
        codeVN.put('ỡ', "097");
        codeVN.put('ợ', "098");
        codeVN.put('ô', "099");
        codeVN.put('ố', "100");
        codeVN.put('ồ', "101");
        codeVN.put('ổ', "102");
        codeVN.put('ỗ', "103");
        codeVN.put('ộ', "104");
        codeVN.put('p', "105");
        codeVN.put('q', "106");
        codeVN.put('r', "107");
        codeVN.put('s', "108");
        codeVN.put('t', "109");
        codeVN.put('u', "110");
        codeVN.put('ú', "111");
        codeVN.put('ù', "112");
        codeVN.put('ủ', "113");
        codeVN.put('ũ', "114");
        codeVN.put('ụ', "115");
        codeVN.put('ư', "116");
        codeVN.put('ứ', "117");
        codeVN.put('ừ', "118");
        codeVN.put('ử', "119");
        codeVN.put('ữ', "120");
        codeVN.put('ự', "121");
        codeVN.put('v', "122");
        codeVN.put('x', "123");
        codeVN.put('y', "124");
        codeVN.put('z', "125");

        codeVN.put('{', "126");
        codeVN.put('|', "127");
        codeVN.put('}', "128");
        codeVN.put('~', "129");
    }

    public String generator(String input) {
        StringBuilder result = new StringBuilder();
        char[] b = input.toLowerCase().toCharArray();
        for (int i = 0; i < b.length; i++) {
            result.append(codeVN.get(b[i]));
        }
        return result.toString();
    }
}
