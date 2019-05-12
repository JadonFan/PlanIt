package course;

public enum AssessmentType {
	ASSIGNMENT(0),
	LAB(1), 
	QUIZ(2),
	EXAMINATION(4),
	;
	
	
	private int id;
	
	
	private AssessmentType(int id) {
		this.id = id;
	}
	
	
	public int getAstmtTypeId() {
		return this.id;
	}


	public static AssessmentType getAstmtTypeById(int id) {
		for (AssessmentType astmtType : AssessmentType.values()) {
			if (astmtType.id == id) {
				return astmtType;
			}
		}
		
		return null;
	}
	
	
	public static AssessmentType getAstmtTypeByRepr(String repr) {
		for (AssessmentType astmtType : AssessmentType.values()) {
			if (astmtType.toString().equals(repr)) {
				return astmtType;
			}
		}
		
		return null;
	}
	
	
	public static String[] reprAllAstmtTypes() {
		String[] astmtTypeStrs = new String[AssessmentType.values().length];
		AssessmentType[] astmtTypes = AssessmentType.values();
		
		for (int index = 0; index < astmtTypeStrs.length; index++) {
			astmtTypeStrs[index] = astmtTypes[index].toString();
		}

		return astmtTypeStrs;
	}
}
