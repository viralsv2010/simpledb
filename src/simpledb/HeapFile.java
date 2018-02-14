package simpledb;

import java.io.*;
import java.util.*;

/**
 * HeapFile is an implementation of a DbFile that stores a collection of tuples in no particular order. Tuples are
 * stored on pages, each of which is a fixed size, and the file is simply a collection of those pages. HeapFile works
 * closely with HeapPage. The format of HeapPages is described in the HeapPage constructor.
 * 
 * @see simpledb.HeapPage#HeapPage
 */
public class HeapFile implements DbFile {

	/**
	 * The File associated with this HeapFile.
	 */
	protected File file;

	/**
	 * The TupleDesc associated with this HeapFile.
	 */
	protected TupleDesc td;

	/**
	 * Constructs a heap file backed by the specified file.
	 * 
	 * @param f
	 *            the file that stores the on-disk backing store for this heap file.
	 */
	public HeapFile(File f, TupleDesc td) {
		this.file = f;
		this.td = td;
	}

	/**
	 * Returns the File backing this HeapFile on disk.
	 * 
	 * @return the File backing this HeapFile on disk.
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Returns an ID uniquely identifying this HeapFile. Implementation note: you will need to generate this tableid
	 * somewhere ensure that each HeapFile has a "unique id," and that you always return the same value for a particular
	 * HeapFile. We suggest hashing the absolute file name of the file underlying the heapfile, i.e.
	 * f.getAbsoluteFile().hashCode().
	 * 
	 * @return an ID uniquely identifying this HeapFile.
	 */
	public int getId() {
		return file.getAbsoluteFile().hashCode();
	}

	/**
	 * Returns the TupleDesc of the table stored in this DbFile.
	 * 
	 * @return TupleDesc of this DbFile.
	 */
	public TupleDesc getTupleDesc() {
		return td;
	}

	// see DbFile.java for javadocs
	public Page readPage(PageId pid) throws NoSuchElementException{
		// Finding the position of the Page with PageID as pid.
		
		int position = pid.pageno() * BufferPool.PAGE_SIZE;
		
//		// reading the HeapFile from the particular position which we found
//		// above.
//		
//		//String readData = readFile(file,position);
//		
//		// Converting the String to bytesArray as we have to return HeapPage 
//		// and HeapPage Constructor is having bytesStream in parameter.
		
		// Creating the Random Access file.
		RandomAccessFile RAccess_file;
		try {
			RAccess_file = new RandomAccessFile(file, "r");
			
			// Using seek function to move the disc arm directly to Specific Position.
			RAccess_file.seek(position);
			
			// Creating the byte array to get the data by bytes. Size of byteArrayData is
			// BufferPool.PAGE_SIZE
			byte[] byteArrayData = new byte[BufferPool.PAGE_SIZE];
			
			// Reading Random Access file data till last byte and storing into the byteArray-
			// Data Created above and will start from position 0.
			RAccess_file.read(byteArrayData, 0 ,BufferPool.PAGE_SIZE );
			
			// pid defines the id of the interface PageID. Type Casting the PageID
			// to HeapPageID as we have to return HeapPage and HeapPage Constructor
			// is having HeapPageId as one of its Parameter.
			
			HeapPageId HeapPage_Id = (HeapPageId) pid;
			HeapPage heap_page;
			
			// Handling the IO Exception by using try catch.
			

				// Creating Reference to HeapPage with id as HeapPageId and
				// byteArrayData as byte Array.
			
				heap_page = new HeapPage(HeapPage_Id, byteArrayData);
				// Resource leak: 'RAccess_file' is not closed at this location
				// I got this error at this page. So, WE need to close the Random access file
				// created.
				RAccess_file.close();
				return heap_page;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Exception :: " + e);
			e.printStackTrace();
		}

		throw new UnsupportedOperationException();
	}

//	private byte readFile(File fileName, int position) {
//		// TODO Auto-generated method stub
//		byte data;
//		try{
//		RandomAccessFile RAccess_file = new RandomAccessFile(file, "r");
//		
//		RAccess_file.seek(position);
//		
//		data = RAccess_file.readByte();
//		}
//		catch(Exception e)
//		{
//			System.out.println("Exception is :: " + e);
//			// Printing the Trace to know where the IO Exception occured and Why it has
//			// occured.
//			
//			e.printStackTrace();
//		}
//		return data;
//	}

	// see DbFile.java for javadocs
	public void writePage(Page page) throws IOException {
		// some code goes here
		// not necessary for assignment1
		throw new UnsupportedOperationException("Implement this");
	}

	/**
	 * Returns the number of pages in this HeapFile.
	 */
	public int numPages() {
		return (int) (file.length() / BufferPool.PAGE_SIZE);
	}

	// see DbFile.java for javadocs
	public ArrayList<Page> addTuple(TransactionId tid, Tuple t)
			throws DbException, IOException, TransactionAbortedException {
		// some code goes here
		// not necessary for assignment1
		throw new UnsupportedOperationException("Implement this");
	}

	// see DbFile.java for javadocs
	public Page deleteTuple(TransactionId tid, Tuple t) throws DbException, TransactionAbortedException {
		// some code goes here
		// not necessary for assignment1
		throw new UnsupportedOperationException("Implement this");
	}

	// see DbFile.java for javadocs
	public DbFileIterator iterator(final TransactionId tid) {

			return new DbFileIterator() {

				/**
				 * The ID of the page to read next.
				 */
				int nextPageID = 0;

				/**
				 * An iterator over all tuples from the page read most recently.
				 */
				Iterator<Tuple> iterator = null;
				//int totalNoOfPages = numPages();
				
				@Override
				public void open() throws DbException, TransactionAbortedException {
					System.out.println("Inside Open :: ");
					nextPageID = 0;
					// obtains an iterator over all tuples from page 0
					iterator = ((HeapPage) Database.getBufferPool().getPage(tid, new HeapPageId(getId(), nextPageID++),Permissions.READ_WRITE)).iterator();
					System.out.println("Inside Open After Iterator :: ");
				}

				@Override
				public boolean hasNext() throws DbException, TransactionAbortedException {
					// some code goes here
					// Checking that the iterator has null value or not.
					// If it is null then we will return false directly as it does not has any
					// values.
					if(iterator == null)
					{
						return false;
					}
					else
					{
					//nextPageID = 0;
//					System.out.println("Before Iterator " + tid +" GetID :: " + getId());
//					HeapPageId hp = new HeapPageId(getId(), nextPageID++);
//					PageId pid = (PageId) hp;
//					int pageid = pid.pageno();
//					System.out.println("\nHP : " + hp + "----- pid " + pid + "------ pageid " + pageid);
//					iterator = ((HeapPage) Database.getBufferPool().getPage(tid, pid  , Permissions.READ_WRITE)).iterator();
//					System.out.println("Iterator :: " + iterator.toString() + " Iterator next :: " + iterator.next().toString());

						if(iterator.hasNext()== true)
						{
							return true;
						}
						else
						{
							HeapPageId hp = new HeapPageId(getId(), nextPageID++);
							iterator = ((HeapPage) Database.getBufferPool().getPage(tid, hp  , Permissions.READ_ONLY)).iterator();
							boolean flag = iterator.hasNext();
							return flag;
						}
					}
						
					//return false;
					//throw new UnsupportedOperationException("Implement this");

				}

				@Override
				public Tuple next() throws DbException, TransactionAbortedException, NoSuchElementException {
					// some code goes here
					if(iterator == null)
					{
						throw new NoSuchElementException();
					}
					else
					{
						return iterator.next();
					}
					// throw new UnsupportedOperationException("Implement this");
				}

				@Override
				public void rewind() throws DbException, TransactionAbortedException {
					close();
					open();
				}

				@Override
				public void close() {
					iterator = null;
				}

			};
		}
		
	}
