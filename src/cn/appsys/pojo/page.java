package cn.appsys.pojo;

public class page {
	private int totalCount = 0;// 总数量

	private int totalPageCount = 1;// 总页数

	private int currentPageNo = 1;// 当前页数

	private int sizePage = 5;// 页面数量

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		setTotalPageCount(totalCount);
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(int totalPageCount) {
			this.totalPageCount = this.totalCount % this.sizePage == 0 ? this.totalCount / this.sizePage
					: (this.totalCount / this.sizePage) + 1;
	}

	public int getCurrentPageNo() {
		return currentPageNo;
	}

	public void setCurrentPageNo(int currentPageNo) {
		if(totalCount==0) {
			currentPageNo=0;
		}
		this.currentPageNo = currentPageNo;
	}

	public int getSizePage() {
		return sizePage;
	}

	public void setSizePage(int sizePage) {
		this.sizePage = sizePage;
	}

}
