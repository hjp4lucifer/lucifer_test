package cn.lucifer.demo.http.domain;

public class PicJson {

	private String parentname;

	private int[] oZoomConfig;

	private ArrPic[] arrPic;

	/**
	 * @return the arrPic
	 * @see #arrPic
	 */
	public ArrPic[] getArrPic() {
		return arrPic;
	}

	/**
	 * @param arrPic
	 *            the arrPic to set
	 * @see #arrPic
	 */
	public void setArrPic(ArrPic[] arrPic) {
		this.arrPic = arrPic;
	}

	/**
	 * @return the parentname
	 * @see #parentname
	 */
	public String getParentname() {
		return parentname;
	}

	/**
	 * @param parentname
	 *            the parentname to set
	 * @see #parentname
	 */
	public void setParentname(String parentname) {
		this.parentname = parentname;
	}

	/**
	 * @return the oZoomConfig
	 * @see #oZoomConfig
	 */
	public int[] getoZoomConfig() {
		return oZoomConfig;
	}

	/**
	 * @param oZoomConfig
	 *            the oZoomConfig to set
	 * @see #oZoomConfig
	 */
	public void setoZoomConfig(int[] oZoomConfig) {
		this.oZoomConfig = oZoomConfig;
	}
}
