/**
 * 
 */
package cn.lucifer.demo.character;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author Lucifer
 * 
 */
public class IOSIcoCharTest {

	static String[] urlencodeArray = { "%23%E2%83%A3", "0%E2%83%A3",
			"1%E2%83%A3", "2%E2%83%A3", "3%E2%83%A3", "4%E2%83%A3",
			"5%E2%83%A3", "6%E2%83%A3", "7%E2%83%A3", "8%E2%83%A3",
			"9%E2%83%A3", "%E2%86%96", "%E2%86%97", "%E2%86%98", "%E2%86%99",
			"%E2%8F%A9", "%E2%8F%AA", "%E2%96%B6", "%E2%97%80", "%E2%98%80",
			"%E2%98%81", "%E2%98%8E", "%E2%98%94", "%E2%98%95", "%E2%98%9D",
			"%E2%98%BA", "%E2%99%88", "%E2%99%89", "%E2%99%8A", "%E2%99%8B",
			"%E2%99%8C", "%E2%99%8D", "%E2%99%8E", "%E2%99%8F", "%E2%99%90",
			"%E2%99%91", "%E2%99%92", "%E2%99%93", "%E2%99%A0", "%E2%99%A3",
			"%E2%99%A5", "%E2%99%A6", "%E2%99%A8", "%E2%99%BF", "%E2%9A%A0",
			"%E2%9A%A1", "%E2%9A%BD", "%E2%9A%BE", "%E2%9B%84", "%E2%9B%8E",
			"%E2%9B%AA", "%E2%9B%B2", "%E2%9B%B3", "%E2%9B%B5", "%E2%9B%BA",
			"%E2%9B%BD", "%E2%9C%82", "%E2%9C%88", "%E2%9C%8A", "%E2%9C%8B",
			"%E2%9C%8C", "%E2%9C%A8", "%E2%9C%B3", "%E2%9C%B4", "%E2%9D%8C",
			"%E2%9D%93", "%E2%9D%94", "%E2%9D%95", "%E2%9D%97", "%E2%9D%A4",
			"%E2%9E%A1", "%E2%9E%BF", "%E2%AC%85", "%E2%AC%86", "%E2%AC%87",
			"%E2%AD%90", "%E2%AD%95", "%E3%80%BD", "%E3%8A%97", "%E3%8A%99",
			"%F0%9F%80%84", "%F0%9F%85%B0", "%F0%9F%85%B1", "%F0%9F%85%BE",
			"%F0%9F%85%BF", "%F0%9F%86%8E", "%F0%9F%86%92", "%F0%9F%86%94",
			"%F0%9F%86%95", "%F0%9F%86%97", "%F0%9F%86%99", "%F0%9F%86%9A",
			"%F0%9F%87%A8%F0%9F%87%B3", "%F0%9F%87%A9%F0%9F%87%AA",
			"%F0%9F%87%AA%F0%9F%87%B8", "%F0%9F%87%AB%F0%9F%87%B7",
			"%F0%9F%87%AC%F0%9F%87%A7", "%F0%9F%87%AE%F0%9F%87%B9",
			"%F0%9F%87%AF%F0%9F%87%B5", "%F0%9F%87%B0%F0%9F%87%B7",
			"%F0%9F%87%B7%F0%9F%87%BA", "%F0%9F%87%BA%F0%9F%87%B8",
			"%F0%9F%88%81", "%F0%9F%88%82", "%F0%9F%88%9A", "%F0%9F%88%AF",
			"%F0%9F%88%B3", "%F0%9F%88%B5", "%F0%9F%88%B6", "%F0%9F%88%B7",
			"%F0%9F%88%B8", "%F0%9F%88%B9", "%F0%9F%88%BA", "%F0%9F%89%90",
			"%F0%9F%8C%80", "%F0%9F%8C%82", "%F0%9F%8C%83", "%F0%9F%8C%84",
			"%F0%9F%8C%85", "%F0%9F%8C%86", "%F0%9F%8C%87", "%F0%9F%8C%88",
			"%F0%9F%8C%8A", "%F0%9F%8C%99", "%F0%9F%8C%9F", "%F0%9F%8C%B4",
			"%F0%9F%8C%B5", "%F0%9F%8C%B7", "%F0%9F%8C%B8", "%F0%9F%8C%B9",
			"%F0%9F%8C%BA", "%F0%9F%8C%BB", "%F0%9F%8C%BE", "%F0%9F%8D%80",
			"%F0%9F%8D%81", "%F0%9F%8D%82", "%F0%9F%8D%83", "%F0%9F%8D%85",
			"%F0%9F%8D%86", "%F0%9F%8D%89", "%F0%9F%8D%8A", "%F0%9F%8D%8E",
			"%F0%9F%8D%93", "%F0%9F%8D%94", "%F0%9F%8D%98", "%F0%9F%8D%99",
			"%F0%9F%8D%9A", "%F0%9F%8D%9B", "%F0%9F%8D%9C", "%F0%9F%8D%9D",
			"%F0%9F%8D%9E", "%F0%9F%8D%9F", "%F0%9F%8D%A1", "%F0%9F%8D%A2",
			"%F0%9F%8D%A3", "%F0%9F%8D%A6", "%F0%9F%8D%A7", "%F0%9F%8D%B0",
			"%F0%9F%8D%B1", "%F0%9F%8D%B2", "%F0%9F%8D%B3", "%F0%9F%8D%B4",
			"%F0%9F%8D%B5", "%F0%9F%8D%B6", "%F0%9F%8D%B8", "%F0%9F%8D%BA",
			"%F0%9F%8D%BB", "%F0%9F%8E%80", "%F0%9F%8E%81", "%F0%9F%8E%82",
			"%F0%9F%8E%83", "%F0%9F%8E%84", "%F0%9F%8E%85", "%F0%9F%8E%86",
			"%F0%9F%8E%87", "%F0%9F%8E%88", "%F0%9F%8E%89", "%F0%9F%8E%8C",
			"%F0%9F%8E%8D", "%F0%9F%8E%8E", "%F0%9F%8E%8F", "%F0%9F%8E%90",
			"%F0%9F%8E%91", "%F0%9F%8E%92", "%F0%9F%8E%93", "%F0%9F%8E%A1",
			"%F0%9F%8E%A2", "%F0%9F%8E%A4", "%F0%9F%8E%A5", "%F0%9F%8E%A6",
			"%F0%9F%8E%A7", "%F0%9F%8E%A8", "%F0%9F%8E%A9", "%F0%9F%8E%AB",
			"%F0%9F%8E%AC", "%F0%9F%8E%AF", "%F0%9F%8E%B0", "%F0%9F%8E%B1",
			"%F0%9F%8E%B5", "%F0%9F%8E%B6", "%F0%9F%8E%B7", "%F0%9F%8E%B8",
			"%F0%9F%8E%BA", "%F0%9F%8E%BE", "%F0%9F%8E%BF", "%F0%9F%8F%80",
			"%F0%9F%8F%81", "%F0%9F%8F%83", "%F0%9F%8F%84", "%F0%9F%8F%86",
			"%F0%9F%8F%88", "%F0%9F%8F%8A", "%F0%9F%8F%A0", "%F0%9F%8F%A2",
			"%F0%9F%8F%A3", "%F0%9F%8F%A5", "%F0%9F%8F%A6", "%F0%9F%8F%A7",
			"%F0%9F%8F%A8", "%F0%9F%8F%A9", "%F0%9F%8F%AA", "%F0%9F%8F%AB",
			"%F0%9F%8F%AC", "%F0%9F%8F%AD", "%F0%9F%8F%AF", "%F0%9F%8F%B0",
			"%F0%9F%90%8D", "%F0%9F%90%8E", "%F0%9F%90%91", "%F0%9F%90%92",
			"%F0%9F%90%94", "%F0%9F%90%97", "%F0%9F%90%98", "%F0%9F%90%99",
			"%F0%9F%90%9A", "%F0%9F%90%9B", "%F0%9F%90%9F", "%F0%9F%90%A0",
			"%F0%9F%90%A4", "%F0%9F%90%A6", "%F0%9F%90%A7", "%F0%9F%90%A8",
			"%F0%9F%90%AB", "%F0%9F%90%AC", "%F0%9F%90%AD", "%F0%9F%90%AE",
			"%F0%9F%90%AF", "%F0%9F%90%B0", "%F0%9F%90%B1", "%F0%9F%90%B3",
			"%F0%9F%90%B4", "%F0%9F%90%B5", "%F0%9F%90%B6", "%F0%9F%90%B7",
			"%F0%9F%90%B8", "%F0%9F%90%B9", "%F0%9F%90%BA", "%F0%9F%90%BB",
			"%F0%9F%91%80", "%F0%9F%91%82", "%F0%9F%91%83", "%F0%9F%91%84",
			"%F0%9F%91%86", "%F0%9F%91%87", "%F0%9F%91%88", "%F0%9F%91%89",
			"%F0%9F%91%8A", "%F0%9F%91%8B", "%F0%9F%91%8C", "%F0%9F%91%8D",
			"%F0%9F%91%8E", "%F0%9F%91%8F", "%F0%9F%91%90", "%F0%9F%91%91",
			"%F0%9F%91%92", "%F0%9F%91%94", "%F0%9F%91%95", "%F0%9F%91%97",
			"%F0%9F%91%98", "%F0%9F%91%99", "%F0%9F%91%9C", "%F0%9F%91%9F",
			"%F0%9F%91%A0", "%F0%9F%91%A1", "%F0%9F%91%A2", "%F0%9F%91%A3",
			"%F0%9F%91%A6", "%F0%9F%91%A7", "%F0%9F%91%A8", "%F0%9F%91%A9",
			"%F0%9F%91%AB", "%F0%9F%91%AE", "%F0%9F%91%AF", "%F0%9F%91%B1",
			"%F0%9F%91%B2", "%F0%9F%91%B3", "%F0%9F%91%B4", "%F0%9F%91%B5",
			"%F0%9F%91%B6", "%F0%9F%91%B7", "%F0%9F%91%B8", "%F0%9F%91%BB",
			"%F0%9F%91%BC", "%F0%9F%91%BD", "%F0%9F%91%BE", "%F0%9F%91%BF",
			"%F0%9F%92%80", "%F0%9F%92%81", "%F0%9F%92%82", "%F0%9F%92%83",
			"%F0%9F%92%84", "%F0%9F%92%85", "%F0%9F%92%86", "%F0%9F%92%87",
			"%F0%9F%92%88", "%F0%9F%92%89", "%F0%9F%92%8A", "%F0%9F%92%8B",
			"%F0%9F%92%8D", "%F0%9F%92%8E", "%F0%9F%92%8F", "%F0%9F%92%90",
			"%F0%9F%92%91", "%F0%9F%92%92", "%F0%9F%92%93", "%F0%9F%92%94",
			"%F0%9F%92%97", "%F0%9F%92%98", "%F0%9F%92%99", "%F0%9F%92%9A",
			"%F0%9F%92%9B", "%F0%9F%92%9C", "%F0%9F%92%9D", "%F0%9F%92%9F",
			"%F0%9F%92%A1", "%F0%9F%92%A2", "%F0%9F%92%A3", "%F0%9F%92%A4",
			"%F0%9F%92%A6", "%F0%9F%92%A8", "%F0%9F%92%A9", "%F0%9F%92%AA",
			"%F0%9F%92%B0", "%F0%9F%92%B1", "%F0%9F%92%B9", "%F0%9F%92%BA",
			"%F0%9F%92%BB", "%F0%9F%92%BC", "%F0%9F%92%BD", "%F0%9F%92%BF",
			"%F0%9F%93%80", "%F0%9F%93%96", "%F0%9F%93%9D", "%F0%9F%93%A0",
			"%F0%9F%93%A1", "%F0%9F%93%A2", "%F0%9F%93%A3", "%F0%9F%93%A9",
			"%F0%9F%93%AB", "%F0%9F%93%AE", "%F0%9F%93%B1", "%F0%9F%93%B2",
			"%F0%9F%93%B3", "%F0%9F%93%B4", "%F0%9F%93%B6", "%F0%9F%93%B7",
			"%F0%9F%93%BA", "%F0%9F%93%BB", "%F0%9F%93%BC", "%F0%9F%94%8A",
			"%F0%9F%94%8D", "%F0%9F%94%91", "%F0%9F%94%92", "%F0%9F%94%93",
			"%F0%9F%94%94", "%F0%9F%94%9D", "%F0%9F%94%9E", "%F0%9F%94%A5",
			"%F0%9F%94%A8", "%F0%9F%94%AB", "%F0%9F%94%AF", "%F0%9F%94%B0",
			"%F0%9F%94%B1", "%F0%9F%94%B2", "%F0%9F%94%B3", "%F0%9F%94%B4",
			"%F0%9F%95%90", "%F0%9F%95%91", "%F0%9F%95%92", "%F0%9F%95%93",
			"%F0%9F%95%94", "%F0%9F%95%95", "%F0%9F%95%96", "%F0%9F%95%97",
			"%F0%9F%95%98", "%F0%9F%95%99", "%F0%9F%95%9A", "%F0%9F%95%9B",
			"%F0%9F%97%BB", "%F0%9F%97%BC", "%F0%9F%97%BD", "%F0%9F%98%81",
			"%F0%9F%98%82", "%F0%9F%98%83", "%F0%9F%98%84", "%F0%9F%98%89",
			"%F0%9F%98%8A", "%F0%9F%98%8C", "%F0%9F%98%8D", "%F0%9F%98%8F",
			"%F0%9F%98%92", "%F0%9F%98%93", "%F0%9F%98%94", "%F0%9F%98%96",
			"%F0%9F%98%98", "%F0%9F%98%9A", "%F0%9F%98%9C", "%F0%9F%98%9D",
			"%F0%9F%98%9E", "%F0%9F%98%A0", "%F0%9F%98%A1", "%F0%9F%98%A2",
			"%F0%9F%98%A3", "%F0%9F%98%A5", "%F0%9F%98%A8", "%F0%9F%98%AA",
			"%F0%9F%98%AD", "%F0%9F%98%B0", "%F0%9F%98%B1", "%F0%9F%98%B2",
			"%F0%9F%98%B3", "%F0%9F%98%B7", "%F0%9F%99%85", "%F0%9F%99%86",
			"%F0%9F%99%87", "%F0%9F%99%8C", "%F0%9F%99%8F", "%F0%9F%9A%80",
			"%F0%9F%9A%83", "%F0%9F%9A%84", "%F0%9F%9A%85", "%F0%9F%9A%87",
			"%F0%9F%9A%89", "%F0%9F%9A%8C", "%F0%9F%9A%8F", "%F0%9F%9A%91",
			"%F0%9F%9A%92", "%F0%9F%9A%93", "%F0%9F%9A%95", "%F0%9F%9A%97",
			"%F0%9F%9A%99", "%F0%9F%9A%9A", "%F0%9F%9A%A2", "%F0%9F%9A%A4",
			"%F0%9F%9A%A5", "%F0%9F%9A%A7", "%F0%9F%9A%AC", "%F0%9F%9A%AD",
			"%F0%9F%9A%B2", "%F0%9F%9A%B6", "%F0%9F%9A%B9", "%F0%9F%9A%BA",
			"%F0%9F%9A%BB", "%F0%9F%9A%BC", "%F0%9F%9A%BD", "%F0%9F%9A%BE",
			"%F0%9F%9B%80" };

	static String[] allOldArray = { "\uE210", "\uE225", "\uE21C", "\uE21D",
			"\uE21E", "\uE21F", "\uE220", "\uE221", "\uE222", "\uE223",
			"\uE224", "\uE237", "\uE236", "\uE238", "\uE239", "\uE23C",
			"\uE23D", "\uE23A", "\uE23B", "\uE04A", "\uE049", "\uE009",
			"\uE04B", "\uE045", "\uE00F", "\uE414", "\uE23F", "\uE240",
			"\uE241", "\uE242", "\uE243", "\uE244", "\uE245", "\uE246",
			"\uE247", "\uE248", "\uE249", "\uE24A", "\uE20E", "\uE20F",
			"\uE20C", "\uE20D", "\uE123", "\uE20A", "\uE252", "\uE13D",
			"\uE018", "\uE016", "\uE048", "\uE24B", "\uE037", "\uE121",
			"\uE014", "\uE01C", "\uE122", "\uE03A", "\uE313", "\uE01D",
			"\uE010", "\uE012", "\uE011", "\uE32E", "\uE206", "\uE205",
			"\uE333", "\uE020", "\uE336", "\uE337", "\uE021", "\uE022",
			"\uE234", "\uE211", "\uE235", "\uE232", "\uE233", "\uE32F",
			"\uE332", "\uE12C", "\uE30D", "\uE315", "\uE12D", "\uE532",
			"\uE533", "\uE535", "\uE14F", "\uE534", "\uE214", "\uE229",
			"\uE212", "\uE24D", "\uE213", "\uE12E", "\uE513", "\uE50E",
			"\uE511", "\uE50D", "\uE510", "\uE50F", "\uE50B", "\uE514",
			"\uE512", "\uE50C", "\uE203", "\uE228", "\uE216", "\uE22C",
			"\uE22B", "\uE22A", "\uE215", "\uE217", "\uE218", "\uE227",
			"\uE22D", "\uE226", "\uE443", "\uE43C", "\uE44B", "\uE04D",
			"\uE449", "\uE146", "\uE44A", "\uE44C", "\uE43E", "\uE04C",
			"\uE335", "\uE307", "\uE308", "\uE304", "\uE030", "\uE032",
			"\uE303", "\uE305", "\uE444", "\uE110", "\uE118", "\uE119",
			"\uE447", "\uE349", "\uE34A", "\uE348", "\uE346", "\uE345",
			"\uE347", "\uE120", "\uE33D", "\uE342", "\uE33E", "\uE341",
			"\uE340", "\uE33F", "\uE339", "\uE33B", "\uE33C", "\uE343",
			"\uE344", "\uE33A", "\uE43F", "\uE046", "\uE34C", "\uE34D",
			"\uE147", "\uE043", "\uE338", "\uE30B", "\uE044", "\uE047",
			"\uE30C", "\uE314", "\uE112", "\uE34B", "\uE445", "\uE033",
			"\uE448", "\uE117", "\uE440", "\uE310", "\uE312", "\uE143",
			"\uE436", "\uE438", "\uE43B", "\uE442", "\uE446", "\uE43A",
			"\uE439", "\uE124", "\uE433", "\uE03C", "\uE03D", "\uE507",
			"\uE30A", "\uE502", "\uE503", "\uE125", "\uE324", "\uE130",
			"\uE133", "\uE42C", "\uE03E", "\uE326", "\uE040", "\uE041",
			"\uE042", "\uE015", "\uE013", "\uE42A", "\uE132", "\uE115",
			"\uE017", "\uE131", "\uE42B", "\uE42D", "\uE036", "\uE038",
			"\uE153", "\uE155", "\uE14D", "\uE154", "\uE158", "\uE501",
			"\uE156", "\uE157", "\uE504", "\uE508", "\uE505", "\uE506",
			"\uE52D", "\uE134", "\uE529", "\uE528", "\uE52E", "\uE52F",
			"\uE526", "\uE10A", "\uE441", "\uE525", "\uE019", "\uE522",
			"\uE523", "\uE521", "\uE055", "\uE527", "\uE530", "\uE520",
			"\uE053", "\uE52B", "\uE050", "\uE52C", "\uE04F", "\uE054",
			"\uE01A", "\uE109", "\uE052", "\uE10B", "\uE531", "\uE524",
			"\uE52A", "\uE051", "\uE419", "\uE41B", "\uE41A", "\uE41C",
			"\uE22E", "\uE22F", "\uE230", "\uE231", "\uE00D", "\uE41E",
			"\uE420", "\uE00E", "\uE421", "\uE41F", "\uE422", "\uE10E",
			"\uE318", "\uE302", "\uE006", "\uE319", "\uE321", "\uE322",
			"\uE323", "\uE007", "\uE13E", "\uE31A", "\uE31B", "\uE536",
			"\uE001", "\uE002", "\uE004", "\uE005", "\uE428", "\uE152",
			"\uE429", "\uE515", "\uE516", "\uE517", "\uE518", "\uE519",
			"\uE51A", "\uE51B", "\uE51C", "\uE11B", "\uE04E", "\uE10C",
			"\uE12B", "\uE11A", "\uE11C", "\uE253", "\uE51E", "\uE51F",
			"\uE31C", "\uE31D", "\uE31E", "\uE31F", "\uE320", "\uE13B",
			"\uE30F", "\uE003", "\uE034", "\uE035", "\uE111", "\uE306",
			"\uE425", "\uE43D", "\uE327", "\uE023", "\uE328", "\uE329",
			"\uE32A", "\uE32B", "\uE32C", "\uE32D", "\uE437", "\uE204",
			"\uE10F", "\uE334", "\uE311", "\uE13C", "\uE331", "\uE330",
			"\uE05A", "\uE14C", "\uE12F", "\uE149", "\uE14A", "\uE11F",
			"\uE00C", "\uE11E", "\uE316", "\uE126", "\uE127", "\uE148",
			"\uE301", "\uE00B", "\uE14B", "\uE142", "\uE317", "\uE103",
			"\uE101", "\uE102", "\uE00A", "\uE104", "\uE250", "\uE251",
			"\uE20B", "\uE008", "\uE12A", "\uE128", "\uE129", "\uE141",
			"\uE114", "\uE03F", "\uE144", "\uE145", "\uE325", "\uE24C",
			"\uE207", "\uE11D", "\uE116", "\uE113", "\uE23E", "\uE209",
			"\uE031", "\uE21A", "\uE21B", "\uE219", "\uE024", "\uE025",
			"\uE026", "\uE027", "\uE028", "\uE029", "\uE02A", "\uE02B",
			"\uE02C", "\uE02D", "\uE02E", "\uE02F", "\uE03B", "\uE509",
			"\uE51D", "\uE404", "\uE412", "\uE057", "\uE415", "\uE405",
			"\uE056", "\uE40A", "\uE106", "\uE402", "\uE40E", "\uE108",
			"\uE403", "\uE407", "\uE418", "\uE417", "\uE105", "\uE409",
			"\uE058", "\uE059", "\uE416", "\uE413", "\uE406", "\uE401",
			"\uE40B", "\uE408", "\uE411", "\uE40F", "\uE107", "\uE410",
			"\uE40D", "\uE40C", "\uE423", "\uE424", "\uE426", "\uE427",
			"\uE41D", "\uE10D", "\uE01E", "\uE435", "\uE01F", "\uE434",
			"\uE039", "\uE159", "\uE150", "\uE431", "\uE430", "\uE432",
			"\uE15A", "\uE01B", "\uE42E", "\uE42F", "\uE202", "\uE135",
			"\uE14E", "\uE137", "\uE30E", "\uE208", "\uE136", "\uE201",
			"\uE138", "\uE139", "\uE151", "\uE13A", "\uE140", "\uE309",
			"\uE13F" };

	static String[] allNewArray = { "\u0023\u20E3", "\u0030\u20E3",
			"\u0031\u20E3", "\u0032\u20E3", "\u0033\u20E3", "\u0034\u20E3",
			"\u0035\u20E3", "\u0036\u20E3", "\u0037\u20E3", "\u0038\u20E3",
			"\u0039\u20E3", "\u2196", "\u2197", "\u2198", "\u2199", "\u23E9",
			"\u23EA", "\u25B6", "\u25C0", "\u2600", "\u2601", "\u260E",
			"\u2614", "\u2615", "\u261D", "\u263A", "\u2648", "\u2649",
			"\u264A", "\u264B", "\u264C", "\u264D", "\u264E", "\u264F",
			"\u2650", "\u2651", "\u2652", "\u2653", "\u2660", "\u2663",
			"\u2665", "\u2666", "\u2668", "\u267F", "\u26A0", "\u26A1",
			"\u26BD", "\u26BE", "\u26C4", "\u26CE", "\u26EA", "\u26F2",
			"\u26F3", "\u26F5", "\u26FA", "\u26FD", "\u2702", "\u2708",
			"\u270A", "\u270B", "\u270C", "\u2728", "\u2733", "\u2734",
			"\u274C", "\u2753", "\u2754", "\u2755", "\u2757", "\u2764",
			"\u27A1", "\u27BF", "\u2B05", "\u2B06", "\u2B07", "\u2B50",
			"\u2B55", "\u303D", "\u3297", "\u3299", "\uD83C\uDC04",
			"\uD83C\uDD70", "\uD83C\uDD71", "\uD83C\uDD7E", "\uD83C\uDD7F",
			"\uD83C\uDD8E", "\uD83C\uDD92", "\uD83C\uDD94", "\uD83C\uDD95",
			"\uD83C\uDD97", "\uD83C\uDD99", "\uD83C\uDD9A",
			"\uD83C\uDDE8\uD83C\uDDF3", "\uD83C\uDDE9\uD83C\uDDEA",
			"\uD83C\uDDEA\uD83C\uDDF8", "\uD83C\uDDEB\uD83C\uDDF7",
			"\uD83C\uDDEC\uD83C\uDDE7", "\uD83C\uDDEE\uD83C\uDDF9",
			"\uD83C\uDDEF\uD83C\uDDF5", "\uD83C\uDDF0\uD83C\uDDF7",
			"\uD83C\uDDF7\uD83C\uDDFA", "\uD83C\uDDFA\uD83C\uDDF8",
			"\uD83C\uDE01", "\uD83C\uDE02", "\uD83C\uDE1A", "\uD83C\uDE2F",
			"\uD83C\uDE33", "\uD83C\uDE35", "\uD83C\uDE36", "\uD83C\uDE37",
			"\uD83C\uDE38", "\uD83C\uDE39", "\uD83C\uDE3A", "\uD83C\uDE50",
			"\uD83C\uDF00", "\uD83C\uDF02", "\uD83C\uDF03", "\uD83C\uDF04",
			"\uD83C\uDF05", "\uD83C\uDF06", "\uD83C\uDF07", "\uD83C\uDF08",
			"\uD83C\uDF0A", "\uD83C\uDF19", "\uD83C\uDF1F", "\uD83C\uDF34",
			"\uD83C\uDF35", "\uD83C\uDF37", "\uD83C\uDF38", "\uD83C\uDF39",
			"\uD83C\uDF3A", "\uD83C\uDF3B", "\uD83C\uDF3E", "\uD83C\uDF40",
			"\uD83C\uDF41", "\uD83C\uDF42", "\uD83C\uDF43", "\uD83C\uDF45",
			"\uD83C\uDF46", "\uD83C\uDF49", "\uD83C\uDF4A", "\uD83C\uDF4E",
			"\uD83C\uDF53", "\uD83C\uDF54", "\uD83C\uDF58", "\uD83C\uDF59",
			"\uD83C\uDF5A", "\uD83C\uDF5B", "\uD83C\uDF5C", "\uD83C\uDF5D",
			"\uD83C\uDF5E", "\uD83C\uDF5F", "\uD83C\uDF61", "\uD83C\uDF62",
			"\uD83C\uDF63", "\uD83C\uDF66", "\uD83C\uDF67", "\uD83C\uDF70",
			"\uD83C\uDF71", "\uD83C\uDF72", "\uD83C\uDF73", "\uD83C\uDF74",
			"\uD83C\uDF75", "\uD83C\uDF76", "\uD83C\uDF78", "\uD83C\uDF7A",
			"\uD83C\uDF7B", "\uD83C\uDF80", "\uD83C\uDF81", "\uD83C\uDF82",
			"\uD83C\uDF83", "\uD83C\uDF84", "\uD83C\uDF85", "\uD83C\uDF86",
			"\uD83C\uDF87", "\uD83C\uDF88", "\uD83C\uDF89", "\uD83C\uDF8C",
			"\uD83C\uDF8D", "\uD83C\uDF8E", "\uD83C\uDF8F", "\uD83C\uDF90",
			"\uD83C\uDF91", "\uD83C\uDF92", "\uD83C\uDF93", "\uD83C\uDFA1",
			"\uD83C\uDFA2", "\uD83C\uDFA4", "\uD83C\uDFA5", "\uD83C\uDFA6",
			"\uD83C\uDFA7", "\uD83C\uDFA8", "\uD83C\uDFA9", "\uD83C\uDFAB",
			"\uD83C\uDFAC", "\uD83C\uDFAF", "\uD83C\uDFB0", "\uD83C\uDFB1",
			"\uD83C\uDFB5", "\uD83C\uDFB6", "\uD83C\uDFB7", "\uD83C\uDFB8",
			"\uD83C\uDFBA", "\uD83C\uDFBE", "\uD83C\uDFBF", "\uD83C\uDFC0",
			"\uD83C\uDFC1", "\uD83C\uDFC3", "\uD83C\uDFC4", "\uD83C\uDFC6",
			"\uD83C\uDFC8", "\uD83C\uDFCA", "\uD83C\uDFE0", "\uD83C\uDFE2",
			"\uD83C\uDFE3", "\uD83C\uDFE5", "\uD83C\uDFE6", "\uD83C\uDFE7",
			"\uD83C\uDFE8", "\uD83C\uDFE9", "\uD83C\uDFEA", "\uD83C\uDFEB",
			"\uD83C\uDFEC", "\uD83C\uDFED", "\uD83C\uDFEF", "\uD83C\uDFF0",
			"\uD83D\uDC0D", "\uD83D\uDC0E", "\uD83D\uDC11", "\uD83D\uDC12",
			"\uD83D\uDC14", "\uD83D\uDC17", "\uD83D\uDC18", "\uD83D\uDC19",
			"\uD83D\uDC1A", "\uD83D\uDC1B", "\uD83D\uDC1F", "\uD83D\uDC20",
			"\uD83D\uDC24", "\uD83D\uDC26", "\uD83D\uDC27", "\uD83D\uDC28",
			"\uD83D\uDC2B", "\uD83D\uDC2C", "\uD83D\uDC2D", "\uD83D\uDC2E",
			"\uD83D\uDC2F", "\uD83D\uDC30", "\uD83D\uDC31", "\uD83D\uDC33",
			"\uD83D\uDC34", "\uD83D\uDC35", "\uD83D\uDC36", "\uD83D\uDC37",
			"\uD83D\uDC38", "\uD83D\uDC39", "\uD83D\uDC3A", "\uD83D\uDC3B",
			"\uD83D\uDC40", "\uD83D\uDC42", "\uD83D\uDC43", "\uD83D\uDC44",
			"\uD83D\uDC46", "\uD83D\uDC47", "\uD83D\uDC48", "\uD83D\uDC49",
			"\uD83D\uDC4A", "\uD83D\uDC4B", "\uD83D\uDC4C", "\uD83D\uDC4D",
			"\uD83D\uDC4E", "\uD83D\uDC4F", "\uD83D\uDC50", "\uD83D\uDC51",
			"\uD83D\uDC52", "\uD83D\uDC54", "\uD83D\uDC55", "\uD83D\uDC57",
			"\uD83D\uDC58", "\uD83D\uDC59", "\uD83D\uDC5C", "\uD83D\uDC5F",
			"\uD83D\uDC60", "\uD83D\uDC61", "\uD83D\uDC62", "\uD83D\uDC63",
			"\uD83D\uDC66", "\uD83D\uDC67", "\uD83D\uDC68", "\uD83D\uDC69",
			"\uD83D\uDC6B", "\uD83D\uDC6E", "\uD83D\uDC6F", "\uD83D\uDC71",
			"\uD83D\uDC72", "\uD83D\uDC73", "\uD83D\uDC74", "\uD83D\uDC75",
			"\uD83D\uDC76", "\uD83D\uDC77", "\uD83D\uDC78", "\uD83D\uDC7B",
			"\uD83D\uDC7C", "\uD83D\uDC7D", "\uD83D\uDC7E", "\uD83D\uDC7F",
			"\uD83D\uDC80", "\uD83D\uDC81", "\uD83D\uDC82", "\uD83D\uDC83",
			"\uD83D\uDC84", "\uD83D\uDC85", "\uD83D\uDC86", "\uD83D\uDC87",
			"\uD83D\uDC88", "\uD83D\uDC89", "\uD83D\uDC8A", "\uD83D\uDC8B",
			"\uD83D\uDC8D", "\uD83D\uDC8E", "\uD83D\uDC8F", "\uD83D\uDC90",
			"\uD83D\uDC91", "\uD83D\uDC92", "\uD83D\uDC93", "\uD83D\uDC94",
			"\uD83D\uDC97", "\uD83D\uDC98", "\uD83D\uDC99", "\uD83D\uDC9A",
			"\uD83D\uDC9B", "\uD83D\uDC9C", "\uD83D\uDC9D", "\uD83D\uDC9F",
			"\uD83D\uDCA1", "\uD83D\uDCA2", "\uD83D\uDCA3", "\uD83D\uDCA4",
			"\uD83D\uDCA6", "\uD83D\uDCA8", "\uD83D\uDCA9", "\uD83D\uDCAA",
			"\uD83D\uDCB0", "\uD83D\uDCB1", "\uD83D\uDCB9", "\uD83D\uDCBA",
			"\uD83D\uDCBB", "\uD83D\uDCBC", "\uD83D\uDCBD", "\uD83D\uDCBF",
			"\uD83D\uDCC0", "\uD83D\uDCD6", "\uD83D\uDCDD", "\uD83D\uDCE0",
			"\uD83D\uDCE1", "\uD83D\uDCE2", "\uD83D\uDCE3", "\uD83D\uDCE9",
			"\uD83D\uDCEB", "\uD83D\uDCEE", "\uD83D\uDCF1", "\uD83D\uDCF2",
			"\uD83D\uDCF3", "\uD83D\uDCF4", "\uD83D\uDCF6", "\uD83D\uDCF7",
			"\uD83D\uDCFA", "\uD83D\uDCFB", "\uD83D\uDCFC", "\uD83D\uDD0A",
			"\uD83D\uDD0D", "\uD83D\uDD11", "\uD83D\uDD12", "\uD83D\uDD13",
			"\uD83D\uDD14", "\uD83D\uDD1D", "\uD83D\uDD1E", "\uD83D\uDD25",
			"\uD83D\uDD28", "\uD83D\uDD2B", "\uD83D\uDD2F", "\uD83D\uDD30",
			"\uD83D\uDD31", "\uD83D\uDD32", "\uD83D\uDD33", "\uD83D\uDD34",
			"\uD83D\uDD50", "\uD83D\uDD51", "\uD83D\uDD52", "\uD83D\uDD53",
			"\uD83D\uDD54", "\uD83D\uDD55", "\uD83D\uDD56", "\uD83D\uDD57",
			"\uD83D\uDD58", "\uD83D\uDD59", "\uD83D\uDD5A", "\uD83D\uDD5B",
			"\uD83D\uDDFB", "\uD83D\uDDFC", "\uD83D\uDDFD", "\uD83D\uDE01",
			"\uD83D\uDE02", "\uD83D\uDE03", "\uD83D\uDE04", "\uD83D\uDE09",
			"\uD83D\uDE0A", "\uD83D\uDE0C", "\uD83D\uDE0D", "\uD83D\uDE0F",
			"\uD83D\uDE12", "\uD83D\uDE13", "\uD83D\uDE14", "\uD83D\uDE16",
			"\uD83D\uDE18", "\uD83D\uDE1A", "\uD83D\uDE1C", "\uD83D\uDE1D",
			"\uD83D\uDE1E", "\uD83D\uDE20", "\uD83D\uDE21", "\uD83D\uDE22",
			"\uD83D\uDE23", "\uD83D\uDE25", "\uD83D\uDE28", "\uD83D\uDE2A",
			"\uD83D\uDE2D", "\uD83D\uDE30", "\uD83D\uDE31", "\uD83D\uDE32",
			"\uD83D\uDE33", "\uD83D\uDE37", "\uD83D\uDE45", "\uD83D\uDE46",
			"\uD83D\uDE47", "\uD83D\uDE4C", "\uD83D\uDE4F", "\uD83D\uDE80",
			"\uD83D\uDE83", "\uD83D\uDE84", "\uD83D\uDE85", "\uD83D\uDE87",
			"\uD83D\uDE89", "\uD83D\uDE8C", "\uD83D\uDE8F", "\uD83D\uDE91",
			"\uD83D\uDE92", "\uD83D\uDE93", "\uD83D\uDE95", "\uD83D\uDE97",
			"\uD83D\uDE99", "\uD83D\uDE9A", "\uD83D\uDEA2", "\uD83D\uDEA4",
			"\uD83D\uDEA5", "\uD83D\uDEA7", "\uD83D\uDEAC", "\uD83D\uDEAD",
			"\uD83D\uDEB2", "\uD83D\uDEB6", "\uD83D\uDEB9", "\uD83D\uDEBA",
			"\uD83D\uDEBB", "\uD83D\uDEBC", "\uD83D\uDEBD", "\uD83D\uDEBE",
			"\uD83D\uDEC0" };

	static char[] old_char_array_Number = new char[] { '\uE210', '\uE225',
			'\uE21C', '\uE21D', '\uE21E', '\uE21F', '\uE220', '\uE221',
			'\uE222', '\uE223', '\uE224' };
	static char[] now_char_array_Number = new char[] { '\u0023', '\u0030',
			'\u0031', '\u0032', '\u0033', '\u0034', '\u0035', '\u0036',
			'\u0037', '\u0038', '\u0039' };

	static char[] old_char_array_D83D = new char[] { '\uE52D', '\uE134',
			'\uE529', '\uE528', '\uE52E', '\uE52F', '\uE526', '\uE10A',
			'\uE441', '\uE525', '\uE019', '\uE522', '\uE523', '\uE521',
			'\uE055', '\uE527', '\uE530', '\uE520', '\uE053', '\uE52B',
			'\uE050', '\uE52C', '\uE04F', '\uE054', '\uE01A', '\uE109',
			'\uE052', '\uE10B', '\uE531', '\uE524', '\uE52A', '\uE051',
			'\uE419', '\uE41B', '\uE41A', '\uE41C', '\uE22E', '\uE22F',
			'\uE230', '\uE231', '\uE00D', '\uE41E', '\uE420', '\uE00E',
			'\uE421', '\uE41F', '\uE422', '\uE10E', '\uE318', '\uE302',
			'\uE006', '\uE319', '\uE321', '\uE322', '\uE323', '\uE007',
			'\uE13E', '\uE31A', '\uE31B', '\uE536', '\uE001', '\uE002',
			'\uE004', '\uE005', '\uE428', '\uE152', '\uE429', '\uE515',
			'\uE516', '\uE517', '\uE518', '\uE519', '\uE51A', '\uE51B',
			'\uE51C', '\uE11B', '\uE04E', '\uE10C', '\uE12B', '\uE11A',
			'\uE11C', '\uE253', '\uE51E', '\uE51F', '\uE31C', '\uE31D',
			'\uE31E', '\uE31F', '\uE320', '\uE13B', '\uE30F', '\uE003',
			'\uE034', '\uE035', '\uE111', '\uE306', '\uE425', '\uE43D',
			'\uE327', '\uE023', '\uE328', '\uE329', '\uE32A', '\uE32B',
			'\uE32C', '\uE32D', '\uE437', '\uE204', '\uE10F', '\uE334',
			'\uE311', '\uE13C', '\uE331', '\uE330', '\uE05A', '\uE14C',
			'\uE12F', '\uE149', '\uE14A', '\uE11F', '\uE00C', '\uE11E',
			'\uE316', '\uE126', '\uE127', '\uE148', '\uE301', '\uE00B',
			'\uE14B', '\uE142', '\uE317', '\uE103', '\uE101', '\uE102',
			'\uE00A', '\uE104', '\uE250', '\uE251', '\uE20B', '\uE008',
			'\uE12A', '\uE128', '\uE129', '\uE141', '\uE114', '\uE03F',
			'\uE144', '\uE145', '\uE325', '\uE24C', '\uE207', '\uE11D',
			'\uE116', '\uE113', '\uE23E', '\uE209', '\uE031', '\uE21A',
			'\uE21B', '\uE219', '\uE024', '\uE025', '\uE026', '\uE027',
			'\uE028', '\uE029', '\uE02A', '\uE02B', '\uE02C', '\uE02D',
			'\uE02E', '\uE02F', '\uE03B', '\uE509', '\uE51D', '\uE404',
			'\uE412', '\uE057', '\uE415', '\uE405', '\uE056', '\uE40A',
			'\uE106', '\uE402', '\uE40E', '\uE108', '\uE403', '\uE407',
			'\uE418', '\uE417', '\uE105', '\uE409', '\uE058', '\uE059',
			'\uE416', '\uE413', '\uE406', '\uE401', '\uE40B', '\uE408',
			'\uE411', '\uE40F', '\uE107', '\uE410', '\uE40D', '\uE40C',
			'\uE423', '\uE424', '\uE426', '\uE427', '\uE41D', '\uE10D',
			'\uE01E', '\uE435', '\uE01F', '\uE434', '\uE039', '\uE159',
			'\uE150', '\uE431', '\uE430', '\uE432', '\uE15A', '\uE01B',
			'\uE42E', '\uE42F', '\uE202', '\uE135', '\uE14E', '\uE137',
			'\uE30E', '\uE208', '\uE136', '\uE201', '\uE138', '\uE139',
			'\uE151', '\uE13A', '\uE140', '\uE309', '\uE13F' };
	static char[] now_char_array_D83D = new char[] { '\uDC0D', '\uDC0E',
			'\uDC11', '\uDC12', '\uDC14', '\uDC17', '\uDC18', '\uDC19',
			'\uDC1A', '\uDC1B', '\uDC1F', '\uDC20', '\uDC24', '\uDC26',
			'\uDC27', '\uDC28', '\uDC2B', '\uDC2C', '\uDC2D', '\uDC2E',
			'\uDC2F', '\uDC30', '\uDC31', '\uDC33', '\uDC34', '\uDC35',
			'\uDC36', '\uDC37', '\uDC38', '\uDC39', '\uDC3A', '\uDC3B',
			'\uDC40', '\uDC42', '\uDC43', '\uDC44', '\uDC46', '\uDC47',
			'\uDC48', '\uDC49', '\uDC4A', '\uDC4B', '\uDC4C', '\uDC4D',
			'\uDC4E', '\uDC4F', '\uDC50', '\uDC51', '\uDC52', '\uDC54',
			'\uDC55', '\uDC57', '\uDC58', '\uDC59', '\uDC5C', '\uDC5F',
			'\uDC60', '\uDC61', '\uDC62', '\uDC63', '\uDC66', '\uDC67',
			'\uDC68', '\uDC69', '\uDC6B', '\uDC6E', '\uDC6F', '\uDC71',
			'\uDC72', '\uDC73', '\uDC74', '\uDC75', '\uDC76', '\uDC77',
			'\uDC78', '\uDC7B', '\uDC7C', '\uDC7D', '\uDC7E', '\uDC7F',
			'\uDC80', '\uDC81', '\uDC82', '\uDC83', '\uDC84', '\uDC85',
			'\uDC86', '\uDC87', '\uDC88', '\uDC89', '\uDC8A', '\uDC8B',
			'\uDC8D', '\uDC8E', '\uDC8F', '\uDC90', '\uDC91', '\uDC92',
			'\uDC93', '\uDC94', '\uDC97', '\uDC98', '\uDC99', '\uDC9A',
			'\uDC9B', '\uDC9C', '\uDC9D', '\uDC9F', '\uDCA1', '\uDCA2',
			'\uDCA3', '\uDCA4', '\uDCA6', '\uDCA8', '\uDCA9', '\uDCAA',
			'\uDCB0', '\uDCB1', '\uDCB9', '\uDCBA', '\uDCBB', '\uDCBC',
			'\uDCBD', '\uDCBF', '\uDCC0', '\uDCD6', '\uDCDD', '\uDCE0',
			'\uDCE1', '\uDCE2', '\uDCE3', '\uDCE9', '\uDCEB', '\uDCEE',
			'\uDCF1', '\uDCF2', '\uDCF3', '\uDCF4', '\uDCF6', '\uDCF7',
			'\uDCFA', '\uDCFB', '\uDCFC', '\uDD0A', '\uDD0D', '\uDD11',
			'\uDD12', '\uDD13', '\uDD14', '\uDD1D', '\uDD1E', '\uDD25',
			'\uDD28', '\uDD2B', '\uDD2F', '\uDD30', '\uDD31', '\uDD32',
			'\uDD33', '\uDD34', '\uDD50', '\uDD51', '\uDD52', '\uDD53',
			'\uDD54', '\uDD55', '\uDD56', '\uDD57', '\uDD58', '\uDD59',
			'\uDD5A', '\uDD5B', '\uDDFB', '\uDDFC', '\uDDFD', '\uDE01',
			'\uDE02', '\uDE03', '\uDE04', '\uDE09', '\uDE0A', '\uDE0C',
			'\uDE0D', '\uDE0F', '\uDE12', '\uDE13', '\uDE14', '\uDE16',
			'\uDE18', '\uDE1A', '\uDE1C', '\uDE1D', '\uDE1E', '\uDE20',
			'\uDE21', '\uDE22', '\uDE23', '\uDE25', '\uDE28', '\uDE2A',
			'\uDE2D', '\uDE30', '\uDE31', '\uDE32', '\uDE33', '\uDE37',
			'\uDE45', '\uDE46', '\uDE47', '\uDE4C', '\uDE4F', '\uDE80',
			'\uDE83', '\uDE84', '\uDE85', '\uDE87', '\uDE89', '\uDE8C',
			'\uDE8F', '\uDE91', '\uDE92', '\uDE93', '\uDE95', '\uDE97',
			'\uDE99', '\uDE9A', '\uDEA2', '\uDEA4', '\uDEA5', '\uDEA7',
			'\uDEAC', '\uDEAD', '\uDEB2', '\uDEB6', '\uDEB9', '\uDEBA',
			'\uDEBB', '\uDEBC', '\uDEBD', '\uDEBE', '\uDEC0' };

	static char[] old_char_array_D83C_4 = { '\uE513', '\uE50E', '\uE511',
			'\uE50D', '\uE510', '\uE50F', '\uE50B', '\uE514', '\uE512',
			'\uE50C' };
	static String[] now_char_array_D83C_4 = { "\uD83C\uDDE8\uD83C\uDDF3",
			"\uD83C\uDDE9\uD83C\uDDEA", "\uD83C\uDDEA\uD83C\uDDF8",
			"\uD83C\uDDEB\uD83C\uDDF7", "\uD83C\uDDEC\uD83C\uDDE7",
			"\uD83C\uDDEE\uD83C\uDDF9", "\uD83C\uDDEF\uD83C\uDDF5",
			"\uD83C\uDDF0\uD83C\uDDF7", "\uD83C\uDDF7\uD83C\uDDFA",
			"\uD83C\uDDFA\uD83C\uDDF8" };

	static char[] old_char_array_D83C = new char[] { '\uE12D', '\uE532',
			'\uE533', '\uE535', '\uE14F', '\uE534', '\uE214', '\uE229',
			'\uE212', '\uE24D', '\uE213', '\uE12E', '\uE203', '\uE228',
			'\uE216', '\uE22C', '\uE22B', '\uE22A', '\uE215', '\uE217',
			'\uE218', '\uE227', '\uE22D', '\uE226', '\uE443', '\uE43C',
			'\uE44B', '\uE04D', '\uE449', '\uE146', '\uE44A', '\uE44C',
			'\uE43E', '\uE04C', '\uE335', '\uE307', '\uE308', '\uE304',
			'\uE030', '\uE032', '\uE303', '\uE305', '\uE444', '\uE110',
			'\uE118', '\uE119', '\uE447', '\uE349', '\uE34A', '\uE348',
			'\uE346', '\uE345', '\uE347', '\uE120', '\uE33D', '\uE342',
			'\uE33E', '\uE341', '\uE340', '\uE33F', '\uE339', '\uE33B',
			'\uE33C', '\uE343', '\uE344', '\uE33A', '\uE43F', '\uE046',
			'\uE34C', '\uE34D', '\uE147', '\uE043', '\uE338', '\uE30B',
			'\uE044', '\uE047', '\uE30C', '\uE314', '\uE112', '\uE34B',
			'\uE445', '\uE033', '\uE448', '\uE117', '\uE440', '\uE310',
			'\uE312', '\uE143', '\uE436', '\uE438', '\uE43B', '\uE442',
			'\uE446', '\uE43A', '\uE439', '\uE124', '\uE433', '\uE03C',
			'\uE03D', '\uE507', '\uE30A', '\uE502', '\uE503', '\uE125',
			'\uE324', '\uE130', '\uE133', '\uE42C', '\uE03E', '\uE326',
			'\uE040', '\uE041', '\uE042', '\uE015', '\uE013', '\uE42A',
			'\uE132', '\uE115', '\uE017', '\uE131', '\uE42B', '\uE42D',
			'\uE036', '\uE038', '\uE153', '\uE155', '\uE14D', '\uE154',
			'\uE158', '\uE501', '\uE156', '\uE157', '\uE504', '\uE508',
			'\uE505', '\uE506' };
	static char[] now_char_array_D83C = new char[] { '\uDC04', '\uDD70',
			'\uDD71', '\uDD7E', '\uDD7F', '\uDD8E', '\uDD92', '\uDD94',
			'\uDD95', '\uDD97', '\uDD99', '\uDD9A', '\uDE01', '\uDE02',
			'\uDE1A', '\uDE2F', '\uDE33', '\uDE35', '\uDE36', '\uDE37',
			'\uDE38', '\uDE39', '\uDE3A', '\uDE50', '\uDF00', '\uDF02',
			'\uDF03', '\uDF04', '\uDF05', '\uDF06', '\uDF07', '\uDF08',
			'\uDF0A', '\uDF19', '\uDF1F', '\uDF34', '\uDF35', '\uDF37',
			'\uDF38', '\uDF39', '\uDF3A', '\uDF3B', '\uDF3E', '\uDF40',
			'\uDF41', '\uDF42', '\uDF43', '\uDF45', '\uDF46', '\uDF49',
			'\uDF4A', '\uDF4E', '\uDF53', '\uDF54', '\uDF58', '\uDF59',
			'\uDF5A', '\uDF5B', '\uDF5C', '\uDF5D', '\uDF5E', '\uDF5F',
			'\uDF61', '\uDF62', '\uDF63', '\uDF66', '\uDF67', '\uDF70',
			'\uDF71', '\uDF72', '\uDF73', '\uDF74', '\uDF75', '\uDF76',
			'\uDF78', '\uDF7A', '\uDF7B', '\uDF80', '\uDF81', '\uDF82',
			'\uDF83', '\uDF84', '\uDF85', '\uDF86', '\uDF87', '\uDF88',
			'\uDF89', '\uDF8C', '\uDF8D', '\uDF8E', '\uDF8F', '\uDF90',
			'\uDF91', '\uDF92', '\uDF93', '\uDFA1', '\uDFA2', '\uDFA4',
			'\uDFA5', '\uDFA6', '\uDFA7', '\uDFA8', '\uDFA9', '\uDFAB',
			'\uDFAC', '\uDFAF', '\uDFB0', '\uDFB1', '\uDFB5', '\uDFB6',
			'\uDFB7', '\uDFB8', '\uDFBA', '\uDFBE', '\uDFBF', '\uDFC0',
			'\uDFC1', '\uDFC3', '\uDFC4', '\uDFC6', '\uDFC8', '\uDFCA',
			'\uDFE0', '\uDFE2', '\uDFE3', '\uDFE5', '\uDFE6', '\uDFE7',
			'\uDFE8', '\uDFE9', '\uDFEA', '\uDFEB', '\uDFEC', '\uDFED',
			'\uDFEF', '\uDFF0' };

	static String[] left_old_array = { "\uE237", "\uE236", "\uE238", "\uE239",
			"\uE23C", "\uE23D", "\uE23A", "\uE23B", "\uE04A", "\uE049",
			"\uE009", "\uE04B", "\uE045", "\uE00F", "\uE414", "\uE23F",
			"\uE240", "\uE241", "\uE242", "\uE243", "\uE244", "\uE245",
			"\uE246", "\uE247", "\uE248", "\uE249", "\uE24A", "\uE20E",
			"\uE20F", "\uE20C", "\uE20D", "\uE123", "\uE20A", "\uE252",
			"\uE13D", "\uE018", "\uE016", "\uE048", "\uE24B", "\uE037",
			"\uE121", "\uE014", "\uE01C", "\uE122", "\uE03A", "\uE313",
			"\uE01D", "\uE010", "\uE012", "\uE011", "\uE32E", "\uE206",
			"\uE205", "\uE333", "\uE020", "\uE336", "\uE337", "\uE021",
			"\uE022", "\uE234", "\uE211", "\uE235", "\uE232", "\uE233",
			"\uE32F", "\uE332", "\uE12C", "\uE30D", "\uE315" };
	static String[] left_now_array = { "\u2196", "\u2197", "\u2198", "\u2199",
			"\u23E9", "\u23EA", "\u25B6", "\u25C0", "\u2600", "\u2601",
			"\u260E", "\u2614", "\u2615", "\u261D", "\u263A", "\u2648",
			"\u2649", "\u264A", "\u264B", "\u264C", "\u264D", "\u264E",
			"\u264F", "\u2650", "\u2651", "\u2652", "\u2653", "\u2660",
			"\u2663", "\u2665", "\u2666", "\u2668", "\u267F", "\u26A0",
			"\u26A1", "\u26BD", "\u26BE", "\u26C4", "\u26CE", "\u26EA",
			"\u26F2", "\u26F3", "\u26F5", "\u26FA", "\u26FD", "\u2702",
			"\u2708", "\u270A", "\u270B", "\u270C", "\u2728", "\u2733",
			"\u2734", "\u274C", "\u2753", "\u2754", "\u2755", "\u2757",
			"\u2764", "\u27A1", "\u27BF", "\u2B05", "\u2B06", "\u2B07",
			"\u2B50", "\u2B55", "\u303D", "\u3297", "\u3299" };

	public static void main(String[] args) {
		String s = "💎";
		toString(s);
	}
	
	public static void main_getAll_UTF_8(String[] args)
			throws UnsupportedEncodingException {
		String s;
		for (int i = 0; i < urlencodeArray.length; i++) {
			s = urlencodeArray[i];
			s = URLDecoder.decode(s, "utf-8");
			toString(s);
		}
	}

	public static void main_D83C(String[] args) {
		System.out.println(left_now_array.length);
		System.out.println(left_now_array.length == left_old_array.length);
		StringBuffer old = new StringBuffer();
		StringBuffer now = new StringBuffer();
		StringBuffer oldLeft = new StringBuffer();
		StringBuffer nowLeft = new StringBuffer();
		for (int i = 0; i < left_now_array.length; i++) {
			if (left_now_array[i].charAt(0) == '\uD83C') {
				old.append(getHexCharacter(left_old_array[i]));
				now.append(getHexCharacter(left_now_array[i].charAt(1) + ""));
			} else {
				oldLeft.append(getHexString(left_old_array[i]));
				nowLeft.append(getHexString(left_now_array[i]));
			}
		}
		System.out.println(old.toString());
		System.out.println(now.toString());
		System.out.println("========================================");
		System.out.println(oldLeft.toString());
		System.out.println(nowLeft.toString());
	}

	public static void main_D83C_4(String[] args) {
		System.out.println(left_now_array.length);
		System.out.println(left_now_array.length == left_old_array.length);
		StringBuffer old = new StringBuffer();
		StringBuffer now = new StringBuffer();
		StringBuffer oldLeft = new StringBuffer();
		StringBuffer nowLeft = new StringBuffer();
		for (int i = 0; i < left_now_array.length; i++) {
			if (left_now_array[i].charAt(0) == '\uD83C'
					&& left_now_array[i].length() == 4) {
				old.append(getHexCharacter(left_old_array[i]));
				now.append(getHexString(left_now_array[i]));
			} else {
				oldLeft.append(getHexString(left_old_array[i]));
				nowLeft.append(getHexString(left_now_array[i]));
			}
		}
		System.out.println(old.toString());
		System.out.println(now.toString());
		System.out.println("========================================");
		System.out.println(oldLeft.toString());
		System.out.println(nowLeft.toString());
	}

	public static void main_D83D(String[] args) {
		System.out.println(left_now_array.length);
		System.out.println(left_now_array.length == left_old_array.length);
		StringBuffer old = new StringBuffer();
		StringBuffer now = new StringBuffer();
		StringBuffer oldLeft = new StringBuffer();
		StringBuffer nowLeft = new StringBuffer();
		for (int i = 0; i < left_now_array.length; i++) {
			if (left_now_array[i].charAt(0) == '\uD83D') {
				old.append(getHexCharacter(left_old_array[i]));
				now.append(getHexCharacter(left_now_array[i].charAt(1) + ""));
			} else {
				oldLeft.append(getHexString(left_old_array[i]));
				nowLeft.append(getHexString(left_now_array[i]));
			}
		}
		System.out.println(old.toString());
		System.out.println(now.toString());
		System.out.println("========================================");
		System.out.println(oldLeft.toString());
		System.out.println(nowLeft.toString());
	}

	public static void main_Number(String[] args) {
		System.out.println(left_now_array.length);
		System.out.println(left_now_array.length == left_old_array.length);
		StringBuffer old = new StringBuffer();
		StringBuffer now = new StringBuffer();
		StringBuffer oldLeft = new StringBuffer();
		StringBuffer nowLeft = new StringBuffer();
		for (int i = 0; i < left_now_array.length; i++) {
			if (left_now_array[i].length() > 1
					&& left_now_array[i].charAt(1) == '\u20E3') {
				// old.append(String.format(format, left_old_array[i]));
				old.append(getHexCharacter(left_old_array[i]));
				// now.append(String.format(",\'%s\'", left_now_array[i]));
				now.append(getHexCharacter(left_now_array[i].charAt(0) + ""));
			} else {
				// oldLeft.append(String.format(formatLeft, left_old_array[i]));
				// nowLeft.append(String.format(formatNowLeft,
				// left_now_array[i].charAt(0)));
				// if (left_now_array[i].length() == 2) {
				// nowLeft.append(String.format(formatNowLeft2,
				// left_now_array[i].charAt(1)));
				// } else {
				// nowLeft.append("\"");
				// }
				oldLeft.append(getHexString(left_old_array[i]));
				nowLeft.append(getHexString(left_now_array[i]));
			}
		}
		System.out.println(old.toString());
		System.out.println(now.toString());
		System.out.println("========================================");
		System.out.println(oldLeft.toString());
		System.out.println(nowLeft.toString());
	}

	public static void toString(String s) {
		System.out.println(getHexString(s));
	}

	public static String getHexString(String s) {
		if (s.length() == 1) {
			return ",\"\\u" + format4(String.format("%H", s.charAt(0))) + "\"";
		}
		StringBuffer buffer = new StringBuffer(",\"");
		for (int i = 0; i < s.length(); i++) {
			String s1 = String.format("%H", s.charAt(i)), s2 = String.format(
					"%H", s.charAt(++i));
			s1 = format4(s1);
			s2 = format4(s2);
			buffer.append("\\u").append(s1).append("\\u").append(s2);
		}
		buffer.append("\"");
		return buffer.toString();
	}

	public static String getHexCharacter(String s) {
		if (s.length() == 1) {
			return ",\'\\u" + format4(String.format("%H", s.charAt(0))) + "\'";
		}
		StringBuffer buffer = new StringBuffer(",\'");
		for (int i = 0; i < s.length(); i++) {
			String s1 = String.format("%H", s.charAt(i)), s2 = String.format(
					"%H", s.charAt(++i));
			s1 = format4(s1);
			s2 = format4(s2);
			buffer.append("\\u").append(s1).append("\\u").append(s2);
		}
		buffer.append("\'");
		return buffer.toString();
	}

	private static String format4(String s) {
		switch (s.length()) {
		case 2:
			s = "00" + s;
			break;
		case 3:
			s = "0" + s;
			break;

		default:
			break;
		}
		return s;
	}
}
