package cn.lucifer.demo.bilibili;

public class GetSource {
	public int code;
	public String message;

	public Result result;

	public class Result {
		public long aid;
		public long cid;
		public int episode_status;
		public Payment payment;
		public String player;
		public int pre_ad;
		public int season_status;
		public String vid;

		public class Payment {
			public String price;
		}
	}

}
