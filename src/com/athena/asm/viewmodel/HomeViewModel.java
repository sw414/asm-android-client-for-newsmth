package com.athena.asm.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.athena.asm.data.Board;
import com.athena.asm.data.MailBox;
import com.athena.asm.data.Profile;
import com.athena.asm.util.SmthSupport;

public class HomeViewModel extends BaseViewModel {
	
	private static final String GUEST_ID = "guest";

	private MailBox m_mailBox = null;
	private List<Board> m_categoryList = null;
	private List<String> m_boardFullStrings = null;
	private HashMap<String, Board> m_boardHashMap = null;

	private Profile m_currentProfile = null;
	
	private boolean m_isLogined = false;
    private boolean m_isGuestLoggedin = false;
    private String m_loginUserID = GUEST_ID;
    
    private String m_currentTab;

	private SmthSupport m_smthSupport;
	
	public static final String CURRENTTAB_PROPERTY_NAME = "CurrentTab";
	public static final String CATEGORYLIST_PROPERTY_NAME = "CategoryList";
	public static final String MAILBOX_PROPERTY_NAME = "Mailbox";
	public static final String PROFILE_PROPERTY_NAME = "Profile";
	
	public HomeViewModel() {
		m_smthSupport = SmthSupport.getInstance();
	}
	
	public boolean isLogined() {
		return m_isLogined;
	}
	
	public void setLoggedin(boolean isUserLoggedin) {
		m_isLogined = isUserLoggedin;
	}
	
	public boolean isGuestLogined() {
		return m_isGuestLoggedin;
	}
	
	public void setGuestLogined(boolean isGuestLoggedin) {
		m_isGuestLoggedin = isGuestLoggedin;
	}
	
	public String getLoginUserID() {
		return m_loginUserID;
	}
	
	public void updateLoginStatus() {
		m_isLogined = m_smthSupport.getLoginStatus();
		if (m_isLogined) {
			m_loginUserID = m_smthSupport.getUserid();
		}
	}
	
	public MailBox getMailBox() {
		return m_mailBox;
	}
	
	public void setMailbox(MailBox mailbox) {
		m_mailBox = mailbox;
	}
	
	public List<Board> getCategoryList() {
		return m_categoryList;
	}
	
	public void setCategoryList(List<Board> categoryList) {
		m_categoryList = categoryList;
	}
	
	public List<String> getBoardFullStrings() {
		return m_boardFullStrings;
	}
	
	public HashMap<String, Board> getBoardHashMap() {
		return m_boardHashMap;
	}
	
	public Profile getCurrentProfile() {
		return m_currentProfile;
	}
	
	public void setCurrentProfile(Profile currentProfile) {
		m_currentProfile = currentProfile;
	}
	
	public String getCurrentTab() {
		return m_currentTab;
	}
	
	public void setCurrentTab(String currentTab) {
		m_currentTab = currentTab;
		
		notifyViewModelChange(this, CURRENTTAB_PROPERTY_NAME);
	}
	
	public boolean login(String userName, String password) {
		m_smthSupport.setUserid(userName);
		m_smthSupport.setPasswd(password);
		return m_smthSupport.login();
	}
	
	public void logout() {
		m_smthSupport.destory();
		
		m_currentTab = null;
		m_loginUserID = GUEST_ID;
		m_isLogined = false;
		m_isGuestLoggedin = false;
		m_currentProfile = null;
		m_mailBox = null;
	}
	
	public void restorSmthSupport() {
		m_smthSupport.restore();
	}
	
	public void updateCategoryList() {
		ArrayList<Board> categoryList = new ArrayList<Board>();
		m_smthSupport.getCategory("TOP", categoryList, false);
		setCategoryList(categoryList);
	}
	
	public void updateMailbox() {
		setMailbox(m_smthSupport.getMailBoxInfo());
	}
	
	public Profile getProfile(String userID) {
		Profile profile = m_smthSupport.getProfile(userID);
		if (userID.equals(m_loginUserID)) {
			setCurrentProfile(profile);
		}
		
		return profile;
	}
	
	public void updateBoardInfo() {
		m_boardFullStrings = new ArrayList<String>();
		m_boardHashMap = new HashMap<String, Board>();
		readBoadInfo(m_categoryList);
	}
	
	private void readBoadInfo(List<Board> boards) {
		for (Iterator<Board> iterator = boards.iterator(); iterator.hasNext();) {
			Board board = (Board) iterator.next();
			if (board != null && board.getEngName() != null) {
				if (!m_boardFullStrings.contains(board.getEngName())) {
					m_boardFullStrings.add(board.getEngName());
				}
				m_boardHashMap.put(board.getEngName().toLowerCase(), board);
			}
			readBoadInfo(board.getChildBoards());
		}
	}
	
	public void notifyCategoryChanged() {
		notifyViewModelChange(this, CATEGORYLIST_PROPERTY_NAME);
	}
	
	public void notifyMailboxChanged() {
		notifyViewModelChange(this, MAILBOX_PROPERTY_NAME);
	}
	
	public void notifyProfileChanged(Profile profile, int step) {
		notifyViewModelChange(this, PROFILE_PROPERTY_NAME, profile, step);
	}
	
}
