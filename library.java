
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class library implements Serializable {
	
	private static final String libraryFile = "library.obj";
	private static final int loanLimit = 2;
	private static final int loanPeriod = 2;
	private static final double finePerDay = 1.0;
	private static final double maxFinesOwed = 1.0;
	private static final double damageFee = 2.0;
	
	private static library SeLf;
	private int bookId; // changed the BOOK_ID to bookId -Amel
	private int memberId; // changed the MEMBER_ID to memberId -Amel
	private int loanId; // changed the LOAN_ID to loanId -Amel
	private Date loanDate; // changed the LOAN_DATE to loanDate -Amel
	
	private Map<Integer, book> CATALOG;
	private Map<Integer, member> MEMBERS;
	private Map<Integer, loan> LOANS;
	private Map<Integer, loan> CURRENT_LOANS;
	private Map<Integer, book> DAMAGED_BOOKS;
	

	private library() {
		CATALOG = new HashMap<>();
		MEMBERS = new HashMap<>();
		LOANS = new HashMap<>();
		CURRENT_LOANS = new HashMap<>();
		DAMAGED_BOOKS = new HashMap<>();
		bookId = 1; // changed the BOOK_ID to bookId -Amel
		memberId = 1;	// changed the MEMBER_ID to memberId -Amel	
		loanId = 1; // changed the LOAN_ID to loanId -Amel		
	}

	
	public static synchronized library INSTANCE() {		
		if (self == null) { // changed the SeLf to self -Amel
			Path PATH = Paths.get(libraryFile);			
			if (Files.exists(PATH)) {	
				try (ObjectInputStream lif = new ObjectInputStream(new FileInputStream(libraryFile));){ // changed the LiF to lif -Amel
			    
					SeLf = (library) lif.readObject(); // changed the LiF to lif -Amel
					Calendar.INSTANCE().Set_dATE(SeLf.LOAN_DATE); // changed the Set_dATE to SetDate -Amel
					lif.close(); // changed the LiF to lif -Amel
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			else self = new library(); // changed the SeLf to self -Amel
		}
		return self; // changed the SeLf to self -Amel
	}

	
	public static synchronized void SAVE() {
		if (self != null) {  // changed the SeLf to self -Amel
			self.LOAN_DATE = Calendar.INSTANCE().Date(); // changed the SeLf to self -Amel
			try (ObjectOutputStream LoF = new ObjectOutputStream(new FileOutputStream(libraryFile));) { // changed the LoF to lof -Amel
				lof.writeObject(SeLf); // changed the LoF to lof -Amel
				lof.flush(); // changed the LoF to lof -Amel
				lof.close(); // changed the LoF to lof -Amel	 
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	
	public int bookId() { // changed the BookID to bookId -Amel	 
		return bookId; // changed the BookID to bookId -Amel
	}
	
	
	public int memberId() { // changed the MEMBER_ID to memberId -Amel
		return memberId; // changed the MEMBER_ID to memberId -Amel
	}
	
	
	private int nextBid() { // changed the NextBID to nextBid -Amel
		return bookId++; // changed the BookID to bookId -Amel
	}

	
	private int nextMid() { // changed the NextMID to nextMid -Amel
		return MEMBER_ID++; // changed the MEMBER_ID to memberId -Amel
	}

	
	private int nextLid() { // changed the NextLID to nextLid -Amel
		return LoanId++; // changed the LOAN_ID to LoanId -Amel
	}

	
	public List<member> MEMBERS() {		
		return new ArrayList<member>(MEMBERS.values()); 
	}


	public List<book> BOOKS() {		
		return new ArrayList<book>(CATALOG.values()); 
	}


	public List<loan> CurrentLoans() {
		return new ArrayList<loan>(CURRENT_LOANS.values());
	}


	public member Add_mem(String lastName, String firstName, String email, int phoneNo) {	 // changed the Add_mem to addMem -Amel	
		member member = new member(lastName, firstName, email, phoneNo, NextMID());
		MEMBERS.put(member.GeT_ID(), member);		
		return member;
	} 

	
	public book Add_book(String a, String t, String c) {	// changed the Add_book to addBook -Amel	
		book b = new book(a, t, c, NextBID());
		CATALOG.put(b.ID(), b);		
		return b;
	}

	
	public member member(int memberId) { // changed the MEMBER to member -Amel
		if (MEMBERS.containsKey(memberId)) 
			return MEMBERS.get(memberId);
		return null;
	}

	
	public book book(int bookId) { // changed the Book to book -Amel
		if (CATALOG.containsKey(bookId)) 
			return CATALOG.get(bookId);		
		return null;
	}

	 
	public int loanLimit() { // changed the LOAN_LIMIT to loanLimit -Amel
		return loanLimit;
	}

	
	public boolean memberCanBorrow(member member) {	 // changed the MEMBER_CAN_BORROW to memberCanBorrow -Amel	
		if (member.Number_Of_Current_Loans() == loanLimit ) 
			return false;
				
		if (member.Fines_OwEd() >= maxFinesOwed) 
			return false;
				
		for (loan loan : member.GeT_LoAnS()) 
			if (loan.OVer_Due()) 
				return false;
			
		return true;
	}

	
	public int loansRemainingForMember(member member) {		 // changed the Loans_Remaining_For_Member to loansRemainingForMember -Amel
		return loanLimit - member.Number_Of_Current_Loans();
	}

	
	public loan issueLaon(book book, member member) { // changed the ISSUE_LAON to issueLaon -Amel
		Date dueDate = Calendar.INSTANCE().Due_Date(loanPeriod);
		loan loan = new loan(NextLID(), book, member, dueDate);
		member.Take_Out_Loan(loan);
		book.Borrow();
		LOANS.put(loan.ID(), loan);
		CURRENT_LOANS.put(book.ID(), loan);
		return loan;
	}
	
	
	public loan loanByBookId(int bookId) { // changed the LOAN_BY_BOOK_ID to loanByBookId -Amel
		if (CURRENT_LOANS.containsKey(bookId)) {
			return CURRENT_LOANS.get(bookId);
		}
		return null;
	}

	
	public double calculateOverDueFine(loan loan) { // changed the CalculateOverDueFine to calculateOverDueFine -Amel
		if (loan.OVer_Due()) {
			long daysOverDue = Calendar.INSTANCE().Get_Days_Difference(loan.Get_Due_Date());
			double fine = daysOverDue * finePerDay;
			return fine;
		}
		return 0.0;		
	}


	public void dischargeLoan(loan currentLoan, boolean isDamaged) { // changed the Discharge_loan to dischargeLoan -Amel
		member member = currentLoan.Member();
		book book  = currentLoan.Book();
		
		double overDueFine = CalculateOverDueFine(currentLoan);
		member.Add_Fine(overDueFine);	
		
		member.dischargeLoan(currentLoan); // changed the dIsChArGeLoAn to dischargeLoan -Amel
		book.Return(isDamaged);
		if (isDamaged) {
			member.Add_Fine(damageFee);
			DAMAGED_BOOKS.put(book.ID(), book);
		}
		currentLoan.DiScHaRgE();
		CURRENT_LOANS.remove(book.ID());
	}


	public void checkCurrentLoans() {
		for (loan loan : CURRENT_LOANS.values()) {
			loan.checkOverDue();
		}		
	}


	public void Repair_BOOK(book currentBook) { // changed the Repair_BOOK to repairBooK -Amel
		if (DAMAGED_BOOKS.containsKey(currentBook.ID())) {
			currentBook.Repair();
			DAMAGED_BOOKS.remove(currentBook.ID());
		}
		else {
			throw new RuntimeException("Library: repairBook: book is not damaged");
		}
		
	}
	
	
}
