package properties;

import java.util.ArrayList;
import java.util.List;

public enum HelpSection {
	NAVDIFF(0, "Navigation too confusing", HelpCategory.GENERAL),
	IMPPRO(1, "Cannot import file", HelpCategory.GENERAL),
	SAVEFAIL(2, "Previous session not saved", HelpCategory.GENERAL)
	;
	
	
	enum HelpCategory {
		GENERAL,
		CONNECTION
		;
	}
	
	
	private int helpId;
	private String helpTitle;
	private HelpCategory helpCategory;
	
	private HelpSection(int helpId, String helpTitle, HelpCategory helpCategory) {
		this.helpId = helpId;
		this.helpTitle = helpTitle;
		this.helpCategory = helpCategory;
	}
	
	
	public HelpSection getHelpSectionById(final int helpId) {
		for (HelpSection section : HelpSection.values()) {
			if (helpId == section.helpId) {
				return section;
			}
		}
		
		return null;
	}
	
	
	public String getHelpTitle() {
		return this.helpTitle;
	}
	
	
	public int getHelpId() {
		return this.helpId;
	}
	
	
	public static List<HelpSection> getAllHelpSectionsinCategory(final HelpCategory helpCategory) {
		List<HelpSection> sections = new ArrayList<>();
		for (HelpSection section : HelpSection.values()) {
			if (helpCategory == section.helpCategory) {
				sections.add(section);
			}
		}
		
		return sections;
	}
}
