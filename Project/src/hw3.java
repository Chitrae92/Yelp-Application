import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.SystemColor;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;

public class hw3 {

	private JFrame frmYelp;
	private JTextField user_review_count_value;
	private JTextArea textArea;
	private JTextField user_friends_count_value;
	private JTextField user_stars_count_value;
	private JComboBox<String> user_reviewcount_comboBox;
	private JComboBox<String> user_friends_comboBox;
	private JComboBox<String> user_stars_comboBox;	
	private JDatePickerImpl user_datePicker;
	private JDatePickerImpl reviewFromdatePicker;
	private JDatePickerImpl reviewTodatePicker;
	private JComboBox<String> select_comboBox;
	db x = new db();
	private JTextField checkinValue;
	private JTextField checkinFromHour;
	private JTextField checkinToHour;
	private JTextField reviewStarValue;
	private JTextField reviewVotesValue;
	private HashSet<String> mainCategory = new HashSet<String>();
	private HashSet<String> subCategory = new HashSet<String>();
	private JPanel subCategoryPanel;
	private JComboBox<String> checkinFromDayComboBox;
	private JComboBox<String> checkinToDayComboBox;
	private JComboBox<String> checkinCountComboBox;
	private JComboBox<String> reviewStarComboBox;
	private JComboBox<String> reviewVotesComboBox;
	public JScrollPane scrollPane ;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					hw3 window = new hw3();
					window.frmYelp.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public hw3() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmYelp = new JFrame();
		frmYelp.setTitle("Yelp");
		frmYelp.getContentPane().setBackground(new Color(0, 0, 51));
		frmYelp.setBounds(100, 100, 1268, 778);
		frmYelp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//User Search
		JButton executeUserQueryButton = new JButton("Execute User Query");
		executeUserQueryButton.setBounds(414, 661, 150, 52);
		executeUserQueryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String type = "user";
				String yelping_since = null;
				String rev_count_cond = null;
				String review_count = null;
				String frnd_count_cond = null;
				String no_of_friends = null;
				String avg_stars_cond = null;
				String avg_stars = null;
				String or_and = null;
				int index = 0;
				if(user_datePicker.getJFormattedTextField().getText()!=null && !user_datePicker.getJFormattedTextField().getText().isEmpty()) {
					yelping_since = user_datePicker.getJFormattedTextField().getText();
					index++;
				}
				if(user_reviewcount_comboBox.getSelectedIndex()>0 && !user_review_count_value.getText().isEmpty()) {
					rev_count_cond = (String)user_reviewcount_comboBox.getSelectedItem();
					review_count = user_review_count_value.getText();
					index++;
				}
				if(user_friends_comboBox.getSelectedIndex()>0 && !user_friends_count_value.getText().isEmpty()) {
					frnd_count_cond = (String)user_friends_comboBox.getSelectedItem();
					no_of_friends = user_friends_count_value.getText();
					index++;
				}
				if(user_stars_comboBox.getSelectedIndex()>0 && !user_stars_count_value.getText().isEmpty()) {
					avg_stars_cond = (String)user_stars_comboBox.getSelectedItem();
					avg_stars = user_stars_count_value.getText();
					index++;
				}
				if(select_comboBox.getSelectedIndex()>0) {
					or_and = (String)select_comboBox.getSelectedItem();
				}
				String result = "";
				if(index==0) {
					textArea.setText("Select user attributes for user query execution");
				}
				else if(index>1 && or_and == null) {
					textArea.setText("Select AND/OR attribute for user query execution");
				}
				else {
					UserSearchQuery a = new UserSearchQuery();
			    	result = a.Query_Yelpuser(yelping_since, rev_count_cond, review_count, frnd_count_cond, no_of_friends, avg_stars_cond, avg_stars, or_and);
			    	textArea.setText(result);
					x.func(result,type,scrollPane);
				}
			}
		});
		frmYelp.getContentPane().setLayout(null);
		executeUserQueryButton.setForeground(Color.WHITE);
		executeUserQueryButton.setBackground(Color.BLUE);
		frmYelp.getContentPane().add(executeUserQueryButton);
		
		JPanel user_panel = new JPanel();
		user_panel.setBounds(10, 504, 366, 212);
		user_panel.setBorder(new LineBorder(new Color(0, 0, 0), 3, true));
		frmYelp.getContentPane().add(user_panel);
		user_panel.setLayout(null);
		UtilDateModel user_model = new UtilDateModel();
		JDatePanelImpl user_date_panel = new JDatePanelImpl(user_model);
		user_datePicker = new JDatePickerImpl(user_date_panel, new DateLabelFormatter());
		user_datePicker.setShowYearButtons(true);
		user_datePicker.setBackground(SystemColor.desktop);
		user_datePicker.setOpaque(false);
		user_datePicker.setBounds(116, 30, 160, 26);
		user_panel.add(user_datePicker);
		user_model.setSelected(true);
		user_model.setValue(null);
		
		JLabel user_member_since = new JLabel("Member since:");
		user_member_since.setBounds(21, 39, 85, 14);
		user_panel.add(user_member_since);
		
		JLabel user_review_count = new JLabel("Review Count:");
		user_review_count.setBounds(21, 67, 85, 14);
		user_panel.add(user_review_count);
		
		JLabel user_avg_stars = new JLabel("Avg. Stars:");
		user_avg_stars.setBounds(21, 125, 85, 14);
		user_panel.add(user_avg_stars);
		
		JLabel user_select = new JLabel("Select:");
		user_select.setBounds(21, 158, 85, 14);
		user_panel.add(user_select);
		
		JLabel user_friends = new JLabel("Num. of Friends:");
		user_friends.setBounds(21, 96, 85, 14);
		user_panel.add(user_friends);
		
		user_reviewcount_comboBox = new JComboBox<String>();
		user_reviewcount_comboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=, >, <", "=", ">", "<" }));
		user_reviewcount_comboBox.setSelectedIndex(0);
		user_reviewcount_comboBox.setBounds(116, 61, 79, 20);
		user_panel.add(user_reviewcount_comboBox);
		
		user_friends_comboBox = new JComboBox<String>();
		user_friends_comboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=, >, <", "=", ">", "<" }));
		user_friends_comboBox.setBounds(116, 93, 79, 20);
		user_panel.add(user_friends_comboBox);
		
		user_stars_comboBox = new JComboBox<String>();
		user_stars_comboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=, >, <", "=", ">", "<" }));
		user_stars_comboBox.setBounds(116, 124, 79, 20);
		user_panel.add(user_stars_comboBox);
		
		select_comboBox = new JComboBox<String>();
		select_comboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AND, OR between attributes", "AND", "OR" }));
		select_comboBox.setBounds(116, 155, 220, 20);
		user_panel.add(select_comboBox);
		
		user_review_count_value = new JTextField();
		user_review_count_value.setBounds(275, 59, 79, 26);
		user_panel.add(user_review_count_value);
		user_review_count_value.setColumns(10);
		
		user_friends_count_value = new JTextField();
		user_friends_count_value.setColumns(10);
		user_friends_count_value.setBounds(275, 91, 79, 26);
		user_panel.add(user_friends_count_value);
		
		user_stars_count_value = new JTextField();
		user_stars_count_value.setColumns(10);
		user_stars_count_value.setBounds(275, 122, 79, 26);
		user_panel.add(user_stars_count_value);
		
		JLabel Users = new JLabel("Users");
		Users.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		Users.setOpaque(true);
		Users.setBackground(SystemColor.activeCaption);
		Users.setHorizontalAlignment(SwingConstants.CENTER);
		Users.setBounds(3, 3, 361, 25);
		user_panel.add(Users);
		
		JLabel user_review_count_valueLabel = new JLabel("value:");
		user_review_count_valueLabel.setBounds(219, 67, 46, 14);
		user_panel.add(user_review_count_valueLabel);
		
		JLabel user_friends_count_valueLabel = new JLabel("value:");
		user_friends_count_valueLabel.setBounds(219, 96, 46, 14);
		user_panel.add(user_friends_count_valueLabel);
		
		JLabel user_stars_count_valueLabel = new JLabel("value:");
		user_stars_count_valueLabel.setBounds(219, 125, 46, 14);
		user_panel.add(user_stars_count_valueLabel);
		
		JScrollPane subCategoryScrollPane = new JScrollPane();
		subCategoryScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		subCategoryScrollPane.setBounds(186, 72, 190, 416);
		subCategoryScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frmYelp.getContentPane().add(subCategoryScrollPane);
		
		subCategoryPanel = new JPanel();
		subCategoryScrollPane.setViewportView(subCategoryPanel);
		subCategoryPanel.setLayout(new BoxLayout(subCategoryPanel, BoxLayout.Y_AXIS));
		
		JScrollPane categoryScrollPane = new JScrollPane();
		categoryScrollPane.setBounds(0, 72, 186, 416);
		categoryScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frmYelp.getContentPane().add(categoryScrollPane);
		
		JPanel categoryPanel = new JPanel();
		categoryScrollPane.setViewportView(categoryPanel);
		categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
		
		//Generating main category checkboxes
		JCheckBox[] checkBoxes = new JCheckBox[28];
		List<String> mainCategories = x.getCategories();
		for(int i=0;i<mainCategories.size();i++) {
			checkBoxes[i] = new JCheckBox(mainCategories.get(i));
			categoryPanel.add(checkBoxes[i]);			
			checkBoxes[i].addMouseListener(new MouseListener(){                                   
                @Override
                public void mouseClicked(MouseEvent e) {
                    JCheckBox mc = (JCheckBox) e.getSource();
					String categoryName = mc.getText();
					if (mc.isSelected()) {
						mainCategory.add(categoryName);
					}
					else {
						mainCategory.remove(categoryName);
					}
					getSubCategories();					
                }
				@Override
				public void mouseEntered(MouseEvent arg0) {
					
				}
				@Override
				public void mouseExited(MouseEvent arg0) {					
				}
				@Override
				public void mousePressed(MouseEvent arg0) {
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {				
				}
            });
		}		
		
		JPanel panel = new JPanel();
		panel.setBounds(383, 398, 445, 252);
		frmYelp.getContentPane().add(panel);
		panel.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setBounds(10, 11, 425, 230);
		panel.add(textArea);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(838, 72, 404, 644);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		frmYelp.getContentPane().add(scrollPane);
				
		JPanel ResultsPanel = new JPanel();
		ResultsPanel.setBounds(838, 42, 404, 30);
		frmYelp.getContentPane().add(ResultsPanel);
		ResultsPanel.setLayout(null);
		
		JLabel label = new JLabel("");
		label.setBounds(202, 5, 0, 0);
		ResultsPanel.add(label);
		
		JLabel ResultLabel = new JLabel("Results");
		ResultLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		ResultLabel.setOpaque(true);
		ResultLabel.setBackground(Color.LIGHT_GRAY);
		ResultLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ResultLabel.setBounds(0, 0, 404, 30);
		ResultsPanel.add(ResultLabel);
		
		JLabel CategoryLabel = new JLabel("Category");
		CategoryLabel.setBounds(0, 42, 186, 30);
		CategoryLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		CategoryLabel.setOpaque(true);
		CategoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
		CategoryLabel.setBackground(Color.LIGHT_GRAY);
		frmYelp.getContentPane().add(CategoryLabel);
		
		JLabel SubCategoryLabel = new JLabel("Sub-Category");
		SubCategoryLabel.setBounds(186, 42, 190, 30);
		SubCategoryLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		SubCategoryLabel.setOpaque(true);
		SubCategoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
		SubCategoryLabel.setBackground(Color.LIGHT_GRAY);
		frmYelp.getContentPane().add(SubCategoryLabel);
		
		JLabel BusinessLabel = new JLabel("Business");
		BusinessLabel.setBounds(0, 21, 376, 22);
		BusinessLabel.setOpaque(true);
		BusinessLabel.setHorizontalAlignment(SwingConstants.CENTER);
		BusinessLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		BusinessLabel.setBackground(SystemColor.activeCaption);
		frmYelp.getContentPane().add(BusinessLabel);
		
		JPanel checkinPanel = new JPanel();
		checkinPanel.setBounds(377, 72, 224, 315);
		frmYelp.getContentPane().add(checkinPanel);
		checkinPanel.setLayout(null);
		
		JLabel checkinFromLabel = new JLabel("from");
		checkinFromLabel.setBounds(14, 11, 57, 20);
		checkinPanel.add(checkinFromLabel);
		
		JLabel checkinToLabel = new JLabel("to");
		checkinToLabel.setBounds(14, 76, 57, 20);
		checkinPanel.add(checkinToLabel);
		
		checkinFromDayComboBox = new JComboBox<String>();
		checkinFromDayComboBox.setBorder(new LineBorder(new Color(171, 173, 179), 1, true));
		checkinFromDayComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Day","SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"}));
		checkinFromDayComboBox.setBounds(14, 33, 99, 26);
		checkinPanel.add(checkinFromDayComboBox);
		
		checkinToDayComboBox = new JComboBox<String>();
		checkinToDayComboBox.setBorder(new LineBorder(new Color(171, 173, 179), 1, true));
		checkinToDayComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Day","SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"}));
		checkinToDayComboBox.setBounds(14, 96, 99, 26);
		checkinPanel.add(checkinToDayComboBox);
		
		JLabel checkinNumOfCheckinsLabel = new JLabel("Num. of Checkins:");
		checkinNumOfCheckinsLabel.setBounds(14, 146, 134, 20);
		checkinPanel.add(checkinNumOfCheckinsLabel);
		
		checkinCountComboBox = new JComboBox<String>();
		checkinCountComboBox.setBorder(new LineBorder(new Color(171, 173, 179), 1, true));
		checkinCountComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=, >, <", "=", ">", "<" }));
		checkinCountComboBox.setSelectedIndex(0);
		checkinCountComboBox.setBounds(66, 177, 117, 26);
		checkinPanel.add(checkinCountComboBox);
		
		checkinValue = new JTextField();
		checkinValue.setBounds(66, 215, 99, 31);
		checkinPanel.add(checkinValue);
		checkinValue.setColumns(10);
		
		checkinFromHour = new JTextField();
		checkinFromHour.setBounds(134, 33, 80, 26);
		checkinPanel.add(checkinFromHour);
		checkinFromHour.setColumns(10);
		
		checkinToHour = new JTextField();
		checkinToHour.setBorder(new LineBorder(new Color(171, 173, 179), 1, true));
		checkinToHour.setColumns(10);
		checkinToHour.setBounds(134, 96, 80, 26);
		checkinPanel.add(checkinToHour);
		
		JLabel checkinValueLabel = new JLabel("value:");
		checkinValueLabel.setBounds(14, 223, 46, 14);
		checkinPanel.add(checkinValueLabel);
		
		JLabel checkinHourLabel = new JLabel("hour");
		checkinHourLabel.setBounds(145, 14, 46, 17);
		checkinPanel.add(checkinHourLabel);
		
		JPanel reviewPanel = new JPanel();
		reviewPanel.setBounds(604, 72, 224, 315);
		frmYelp.getContentPane().add(reviewPanel);
		reviewPanel.setLayout(null);
		UtilDateModel review_model = new UtilDateModel();
		JDatePanelImpl review_date_panel = new JDatePanelImpl(review_model);
		reviewFromdatePicker = new JDatePickerImpl(review_date_panel, new DateLabelFormatter());
		reviewFromdatePicker.setShowYearButtons(true);
		reviewFromdatePicker.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		reviewFromdatePicker.setBackground(SystemColor.desktop);
		reviewFromdatePicker.setOpaque(false);
		reviewFromdatePicker.setBounds(48, 22, 160, 27);
		reviewPanel.add(reviewFromdatePicker);
		review_model.setSelected(true);
		review_model.setValue(null);
		UtilDateModel review1_model = new UtilDateModel();
		JDatePanelImpl review1_date_panel = new JDatePanelImpl(review1_model);
		reviewTodatePicker = new JDatePickerImpl(review1_date_panel, new DateLabelFormatter());
		reviewTodatePicker.setShowYearButtons(true);
		reviewTodatePicker.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		reviewTodatePicker.setBackground(SystemColor.desktop);
		reviewTodatePicker.setOpaque(false);
		reviewTodatePicker.setBounds(48, 72, 160, 27);
		reviewPanel.add(reviewTodatePicker);	
		review1_model.setSelected(true);
		review1_model.setValue(null);
		
		JLabel reviewFromLabel = new JLabel("from");
		reviewFromLabel.setBounds(10, 22, 28, 20);
		reviewPanel.add(reviewFromLabel);
		
		JLabel reviewToLabel = new JLabel("to");
		reviewToLabel.setBounds(10, 72, 28, 20);
		reviewPanel.add(reviewToLabel);
		
		JLabel reviewStarLabel = new JLabel("star:");
		reviewStarLabel.setBounds(10, 127, 50, 20);
		reviewPanel.add(reviewStarLabel);
		
		JLabel reviewStarsValueLabel = new JLabel("value:");
		reviewStarsValueLabel.setBounds(10, 168, 50, 20);
		reviewPanel.add(reviewStarsValueLabel);
		
		JLabel reviewVotesLabel = new JLabel("votes:");
		reviewVotesLabel.setBounds(10, 211, 50, 20);
		reviewPanel.add(reviewVotesLabel);
		
		JLabel reviewVotesValueLabel = new JLabel("value:");
		reviewVotesValueLabel.setBounds(10, 253, 50, 20);
		reviewPanel.add(reviewVotesValueLabel);
		
		reviewVotesComboBox = new JComboBox<String>();
		reviewVotesComboBox.setBorder(new LineBorder(new Color(171, 173, 179), 1, true));
		reviewVotesComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=, >, <", "=", ">", "<" }));
		reviewVotesComboBox.setSelectedIndex(0);
		reviewVotesComboBox.setBounds(70, 211, 100, 20);
		reviewPanel.add(reviewVotesComboBox);
		
		reviewStarComboBox = new JComboBox<String>();
		reviewStarComboBox.setBorder(new LineBorder(new Color(171, 173, 179), 1, true));
		reviewStarComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=, >, <", "=", ">", "<" }));
		reviewStarComboBox.setSelectedIndex(0);
		reviewStarComboBox.setBounds(70, 127, 100, 20);
		reviewPanel.add(reviewStarComboBox);
		
		reviewStarValue = new JTextField();
		reviewStarValue.setColumns(10);
		reviewStarValue.setBounds(70, 168, 79, 26);
		reviewPanel.add(reviewStarValue);
		
		reviewVotesValue = new JTextField();
		reviewVotesValue.setColumns(10);
		reviewVotesValue.setBounds(70, 253, 79, 26);
		reviewPanel.add(reviewVotesValue);
		
		JLabel CheckinLabel = new JLabel("Checkin");
		CheckinLabel.setBounds(376, 42, 226, 30);
		CheckinLabel.setOpaque(true);
		CheckinLabel.setHorizontalAlignment(SwingConstants.CENTER);
		CheckinLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		CheckinLabel.setBackground(Color.LIGHT_GRAY);
		frmYelp.getContentPane().add(CheckinLabel);
		
		JLabel ReviewLabel = new JLabel("Review");
		ReviewLabel.setBounds(603, 42, 226, 30);
		ReviewLabel.setOpaque(true);
		ReviewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ReviewLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		ReviewLabel.setBackground(Color.LIGHT_GRAY);
		frmYelp.getContentPane().add(ReviewLabel);		
		
		//Business Search
		JButton executeBusinessQueryButton = new JButton("Execute Business Query");
		executeBusinessQueryButton.setForeground(Color.WHITE);
		executeBusinessQueryButton.setBackground(Color.BLUE);
		executeBusinessQueryButton.setBounds(614, 661, 186, 52);
		frmYelp.getContentPane().add(executeBusinessQueryButton);
		executeBusinessQueryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String type = "business";
				String checkinQuery = getCheckin();
				String reviewQuery = getReview();
				BusinessSearchQuery a = new BusinessSearchQuery();	
				if(mainCategory.size()!=0) {
			    	String businessQuery = a.queryBusiness(mainCategory,subCategory,checkinQuery,reviewQuery);
			    	textArea.setText(businessQuery);
					x.func(businessQuery,type,scrollPane);
				}
				else 
					textArea.setText("Please select a business category for execution of Business Query");
			}			
		});
			
	}
	
		//Generating Subcategories checkboxes
		public void getSubCategories() {	
			subCategoryPanel.removeAll();
			List<String> subCategories = new ArrayList<String>();
			if(mainCategory.size()!=0) {
			BusinessSearchQuery b = new BusinessSearchQuery();
			String categoryList = b.generateCategoriesINFormat(mainCategory);
			subCategories = x.getSubCategories(categoryList);}
				for(int j=0;j<subCategories.size();j++) {
					JCheckBox cb = new JCheckBox(subCategories.get(j));
					subCategoryPanel.add(cb);
					subCategoryPanel.revalidate();
					cb.addMouseListener(new MouseListener(){                                   
		                @Override
		                public void mouseClicked(MouseEvent e) {
		                    JCheckBox sc = (JCheckBox) e.getSource();
							String subCategoryName = sc.getText();
							if (sc.isSelected()) {
								subCategory.add(subCategoryName);
							}
							else {
								subCategory.remove(subCategoryName);
							}	
		                }	                
		                
						@Override
						public void mouseEntered(MouseEvent arg0) {
							
						}
						@Override
						public void mouseExited(MouseEvent arg0) {					
						}
						@Override
						public void mousePressed(MouseEvent arg0) {
						}
		
						@Override
						public void mouseReleased(MouseEvent arg0) {				
						}
		            	});								
					}
				subCategoryPanel.updateUI();
		}
		
		//Get business checkin attributes
		public String getCheckin() {
			String fromDay = null;
			String fromHr = null;
			String toDay = null;
			String toHr = null;
			String checkinCond = null;
			String noOfCheckin = null;
			int from_day = -1;
			int to_day = -1;
			String checkinQuery = "";	
			int index1 = 0;	
			int index2 = 0;	
			if(checkinFromDayComboBox.getSelectedIndex()>0 && !checkinFromHour.getText().isEmpty()) {
					fromDay = (String)checkinFromDayComboBox.getSelectedItem();
					if(fromDay.equals("SUNDAY")) {
						from_day = 0;
					}
					else if(fromDay.equals("MONDAY")) {
						from_day = 1;
					}
					else if(fromDay.equals("TUESDAY")) {
						from_day = 2;
					}
					else if(fromDay.equals("WEDNESDAY")) {
						from_day = 3;
					}
					else if(fromDay.equals("THURSDAY")) {
						from_day = 4;
					}
					else if(fromDay.equals("FRIDAY")) {
						from_day = 5;
					}
					else if(fromDay.equals("SATURDAY")) {
						from_day = 6;
					}
					fromHr = checkinFromHour.getText();		
					index1++;
			}	
			if(checkinToDayComboBox.getSelectedIndex()>0 && !checkinToHour.getText().isEmpty()) {
				toDay = (String)checkinToDayComboBox.getSelectedItem();
				if(toDay.equals("SUNDAY")) {
					to_day = 0;
				}
				else if(toDay.equals("MONDAY")) {
					to_day = 1;
				}
				else if(toDay.equals("TUESDAY")) {
					to_day = 2;
				}
				else if(toDay.equals("WEDNESDAY")) {
					to_day = 3;
				}
				else if(toDay.equals("THURSDAY")) {
					to_day = 4;
				}
				else if(toDay.equals("FRIDAY")) {
					to_day = 5;
				}
				else if(toDay.equals("SATURDAY")) {
					to_day = 6;
				}				
				toHr = checkinToHour.getText();		
				index1++;
			}			
			if(checkinCountComboBox.getSelectedIndex()>0 && !checkinValue.getText().isEmpty()) {
				checkinCond = (String)checkinCountComboBox.getSelectedItem();
				noOfCheckin = checkinValue.getText();
				index2++;
			}	
			if(index1<2 && index2==0) {
				return checkinQuery;
			}
			else {
				BusinessSearchQuery b = new BusinessSearchQuery();
				checkinQuery = b.queryCheckin(from_day,fromHr,to_day,toHr,noOfCheckin,checkinCond);	
				return checkinQuery;
			}
		}
		
		//Get business review attributes
		public String getReview() {
			String fromDate = null;
			String toDate = null;
			String starCond = null;
			String starValue = null;
			String votesCond = null;
			String votesValue = null;
			String reviewQuery = "";	
			int index=0;			
			if(reviewFromdatePicker.getJFormattedTextField().getText()!=null && !reviewFromdatePicker.getJFormattedTextField().getText().isEmpty()) {
				fromDate = reviewFromdatePicker.getJFormattedTextField().getText();
				index++;
			}
			if(reviewTodatePicker.getJFormattedTextField().getText()!=null && !reviewTodatePicker.getJFormattedTextField().getText().isEmpty()) {
				toDate = reviewTodatePicker.getJFormattedTextField().getText();
				index++;
			}
			if(reviewStarComboBox.getSelectedIndex()>0 && !reviewStarValue.getText().isEmpty()) {
				starCond = (String)reviewStarComboBox.getSelectedItem();
				starValue = reviewStarValue.getText();
				index++;
			}
			if(reviewVotesComboBox.getSelectedIndex()>0 && !reviewVotesValue.getText().isEmpty()) {
				votesCond = (String)reviewVotesComboBox.getSelectedItem();
				votesValue = reviewVotesValue.getText();
				index++;
			}
			if(index==0) {
				return reviewQuery;
			} 
			BusinessSearchQuery b = new BusinessSearchQuery();
			reviewQuery = b.queryReview(fromDate,toDate,starCond,starValue,votesCond,votesValue);	
			return reviewQuery;
		}
}
