package simpledb;

import java.io.*;
import java.util.*;

public class temp {
	
	protected Hashtable<PageId, Page> pages;
	
	public void main(String[] args) {
		for (PageId name: pages.keySet()){

            String key =name.toString();
            String value = pages.get(name).toString();  
            System.out.println("\n Pages Key Value :: "+ key + " " + value);
            }
}
}