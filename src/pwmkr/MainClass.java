package pwmkr;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MainClass
{
	//private final static String alphaClass = "[a-zA-Z]";
	//private final static String alphaNumClass = "[a-zA-Z0-9@]";
	private final static String symbolClass = "[a-zA-Z0-9!@#$%^&*()_+=,.<>/';:{}]";
	
	static void threadMessage(String message) 
	{
        String threadName = Thread.currentThread().getName();
        System.out.format("%s: %s%n", threadName, message);
    }
	
	// Class that denotes a running thread for this application
	private static class MessageLoop implements Runnable 
	{
        private int start;
        private int length;
        public static Set<String> pws;
        private String A,B;
        private Algorithm algInstance;
        
        // Constructor for the MessageLoop class, initializes all default values and creates an instance of the Algorithm class for each thread
        public MessageLoop(int i)
        {
        	start = i;
        	length = 15;
        	if(pws == null)
        	{
        		pws = Collections.synchronizedSet(new HashSet<String>());
        	}
        	A = "yahoo";
        	B = "xz62rP";
        	algInstance = new Algorithm("test");
        }

		public void run() 
		{
			try 
			{
                // Tries a set number of passwords and flags non-unique passwords
				// At the end of the loop, prints out the total number of unique passwords in the set for the terminated thread
				for (int i = start; i<10000000; i += 20) 
                {
					String temp = algInstance.getPW("test", A, B, i, 0, length);
					if(!pws.add(temp))
					{
						System.out.println("duplicate found: "+ temp + " input was: " + i);
					}
					else
					{
						if(pws.size()%25000 == 0)
						{
							System.out.println("added: " + temp + " is the " + pws.size() + "th term");
						}
					}
                }
                threadMessage("terminated with: " + Integer.valueOf(pws.size()).toString() + " in pws");
            } 
			catch (FileNotFoundException e) 
            {
                threadMessage("I wasn't done!");
            }
		}
    }
	
	// Gives an hour,minute,second representation of an input nanotime
	private static String getTime(long inNano)
	{
		long second = 1000000000;
		long minute = 60 * second;
		long hour = 60 * minute;
		
		long hours = inNano / hour;
		long minutes = (inNano % hour)/minute;
		long seconds = (inNano % minute)/second;
		
		String ret = Long.valueOf(hours).toString() + " hours, " + Long.valueOf(minutes).toString() + " minutes, " + Long.valueOf(seconds).toString() + " seconds";
		return ret;
	}
	
	public static void main(String[] args)
	{
		GenerateFile.seedGen("test", symbolClass, 100, 100, 1000);
		long start = System.nanoTime();
		
		// Create and start all the threads (20 in all)
		Thread a = new Thread(new MessageLoop(0));
		Thread b = new Thread(new MessageLoop(1));
		Thread c = new Thread(new MessageLoop(2));
		Thread d = new Thread(new MessageLoop(3));
		Thread e = new Thread(new MessageLoop(4));
		Thread f = new Thread(new MessageLoop(5));
		Thread g = new Thread(new MessageLoop(6));
		Thread h = new Thread(new MessageLoop(7));
		Thread i = new Thread(new MessageLoop(8));
		Thread j = new Thread(new MessageLoop(9));
		Thread k = new Thread(new MessageLoop(10));
		Thread l = new Thread(new MessageLoop(11));
		Thread m = new Thread(new MessageLoop(12));
		Thread n = new Thread(new MessageLoop(13));
		Thread o = new Thread(new MessageLoop(14));
		Thread p = new Thread(new MessageLoop(15));
		Thread q = new Thread(new MessageLoop(16));
		Thread r = new Thread(new MessageLoop(17));
		Thread s = new Thread(new MessageLoop(18));
		Thread t = new Thread(new MessageLoop(19));
		a.start();
		b.start();
		c.start();
		d.start();
		e.start();
		f.start();
		g.start();
		h.start();
		i.start();
		j.start();
		k.start();
		l.start();
		m.start();
		n.start();
		o.start();
		p.start();
		q.start();
		r.start();
		s.start();
		t.start();	
		
		// Check that all threads have exited before completing
		while(a.isAlive() || b.isAlive() || c.isAlive() || d.isAlive() || e.isAlive() || f.isAlive() || 
				g.isAlive() || h.isAlive() || i.isAlive() || j.isAlive() || k.isAlive() || l.isAlive() ||
				m.isAlive() || n.isAlive() || o.isAlive() || p.isAlive() || q.isAlive() || r.isAlive() ||
				s.isAlive() || t.isAlive()) { }
		
		long end = System.nanoTime();
		System.out.println("total unique: " + MessageLoop.pws.size());
		System.out.println(getTime(end-start));
	}
}
