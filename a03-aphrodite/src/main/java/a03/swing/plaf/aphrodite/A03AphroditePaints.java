package a03.swing.plaf.aphrodite;

import java.awt.Color;

public interface A03AphroditePaints {
	
	public static final Color CONTROL = new Color(0xd6d6d6);

	public static final Color ROOT_OUTER_BORDER_ENABLED_COLOR = new Color(0x7f7f7f);
	
	public static final Color ROOT_INNER_BORDER_ENABLED_COLOR = new Color(0xf0f0f0);
	
	public static final Color SCROLL_BAR_BORDER_COLOR = new Color(0xb3b3b3);

	public static final Color POPUPMENU_BACKGROUND_COLOR = new Color(0xdfe2e7);

	public static final Color BUTTON_FOREGROUND_ENABLED_COLOR = new Color(0x191919);

	public static final Color BUTTON_FOREGROUND_ENABLED_ARMED_COLOR = new Color(0x191919);

	public static final Color BUTTON_FOREGROUND_DISABLED_COLOR = new Color(0x191919);

	public static final Color BUTTON_FOREGROUND_DISABLED_ARMED_COLOR = new Color(0x191919);

	public static final Color BUTTON_SHADOW_FOREGROUND_ENABLED_COLOR = Color.WHITE; // new Color(0xdfdfdf);

	public static final Color BUTTON_SHADOW_FOREGROUND_ENABLED_ARMED_COLOR = new Color(0xb3b8ad);

	public static final Color BUTTON_SHADOW_FOREGROUND_DISABLED_COLOR = new Color(0xa9a9a9);

	public static final Color BUTTON_SHADOW_FOREGROUND_DISABLED_ARMED_COLOR = new Color(0xa9a9a9);

	
	public static final Color[] BUTTON_OUTER_BORDER_ENABLED_COLORS = new Color[] {
		new Color(0xd6d6d6),
		new Color(0xf3f3f3)
	};
	
	public static final Color[] BUTTON_OUTER_BORDER_ENABLED_ARMED_COLORS = new Color[] {
		new Color(0xd6d6d6),
		new Color(0xf1f1f1)
	};

	public static final Color[] BUTTON_OUTER_BORDER_DISABLED_COLORS = new Color[] {
		CONTROL,
		new Color(0xf3f3f3)
	};
	
	public static final Color[] BUTTON_OUTER_BORDER_DISABLED_ARMED_COLORS = new Color[] {
		CONTROL,
		new Color(0xf1f1f1)
	};
	
	public static final Color BUTTON_BORDER_ENABLED_COLOR = new Color(0x737373);
	
	public static final Color BUTTON_BORDER_ENABLED_ARMED_COLOR = new Color(0x666666);

	public static final Color BUTTON_BORDER_DISABLED_COLOR = new Color(0x838383);
	
	public static final Color BUTTON_BORDER_DISABLED_ARMED_COLOR = new Color(0x777777);
	

	public static final Color[] BUTTON_BACKGROUND_ENABLED_COLORS = new Color[] {
		new Color(0xe7e7e7),
		new Color(0xcacaca)
	};
	
	public static final Color[] BUTTON_BACKGROUND_ENABLED_ARMED_COLORS = new Color[] {
		Color.WHITE,
		new Color(0xe7e7e7)
	};
	
	public static final Color[] BUTTON_BACKGROUND_DISABLED_COLORS = new Color[] {
//		new Color(0xf7f7f7),
//		new Color(0xeaeaea)
		new Color(0xb7b7b7),
		new Color(0x909090)
	};
	
	public static final Color[] BUTTON_BACKGROUND_DISABLED_ARMED_COLORS = new Color[] {
//		new Color(0xd7d7d7),
//		new Color(0xb0b0b0)
		new Color(0xd7d7d7),
		new Color(0xb0b0b0)
	};
	
	public static final Color FOCUS_COLOR = new Color(0x7a98af);

	public final static Color ROW_ARMED_COLOR = new Color(0xc5dbf2); // new Color(0x929297);
	
	public final static Color[] ROW_COLORS = new Color[] { Color.WHITE, new Color(0xf1f5fa) };

	
//	public final static Color[] TABBED_PANE_TAB_BORDER_ENABLED_COLORS = new Color[] {
//			new Color(0xebebeb),
//			new Color(0xd6d6d6)
//	};
//
//	public final static Color[] TABBED_PANE_TAB_BORDER_ENABLED_ARMED_COLORS = new Color[] {
//		new Color(0xebebeb),
//		new Color(0xd6d6d6)
//	};
//
//	public final static Color[] TABBED_PANE_TAB_BORDER_DISABLED_ARMED_COLORS = new Color[] {
//		new Color(0xebebeb),
//		new Color(0xd6d6d6)
//	};
//
//	public final static Color[] TABBED_PANE_TAB_BORDER_DISABLED_COLORS = new Color[] {
//		new Color(0xebebeb),
//		new Color(0xd6d6d6)
//	};

	public final static Color[] TABBED_PANE_TAB_BOTTOM_BACKGROUND_DISABLED_COLORS = new Color[] {
		new Color(0x828282),						
		new Color(0x9a9a9a)
	};

	public final static Color[] TABBED_PANE_TAB_BACKGROUND_ENABLED_ARMED_COLORS = new Color[] {
		Color.WHITE,
		new Color(0xe7e7e7)
	};

	public final static Color[] TABBED_PANE_TAB_BACKGROUND_ENABLED_COLORS = new Color[] {
		new Color(0xaaaaaa),
		new Color(0x929292)						
	};

	public final static Color[] TABBED_PANE_TAB_BACKGROUND_DISABLED_ARMED_COLORS = new Color[] {
		new Color(0x9a9a9a),
		new Color(0x828282)						
	};

	public final static Color[] TABBED_PANE_TAB_BACKGROUND_DISABLED_COLORS = new Color[] {
		new Color(0x9a9a9a),
		new Color(0x828282)						
	};

	public final static Color[] PROGRESS_BAR_AMOUNT_ENABLED_BACKGROUND = new Color[] { 
		new Color(0xc0f5c0), 
//		new Color(0x69d27b), 
//		new Color(0x00ae1c), 
		new Color(0x1dc12f) };
	
	public final static Color[] PROGRESS_BAR_AMOUNT_DISABLED_BACKGROUND = new Color[] { 
		new Color(0xdbf8dc), 
//		new Color(0xb4e9bd), 
//		new Color(0x81d88f), 
		new Color(0x8bde95) };

	
//	public final static Color[] ROOT_PANE_BORDER_COLORS = new Color[] { 
//	new Color(0xb5bcc0),
//	new Color(0x959ea1) };
	
	public final static Color[] TITLE_BAR_ENABLED_BACKGROUND_COLORS = new Color[] { 
		new Color(0xcacaca),
		new Color(0xe7e7e7)
	};
	
	public final static Color TABBED_PANE_CONTENT_BORDER_COLOR = new Color(0x5e5e5e);
}
