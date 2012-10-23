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
	
	private final static String[] numTest = {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};
	private final static String[] alphaTest = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y"};
	private final static String[] aniTest = {"monkey","mouse","squirrel","cat","dog","horse","ant","spider","rabbit","lion","tiger","cougar","elephant",
											"llama","tapir","panther","worm","fish","dolphin","whale","mollusk","snake","lizard","frog","salamander"};
	
	private static String filename;
	
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
        public static Set<String> dpws;
        private String A,B,Loop,Type;
        private Algorithm algInstance;
        
        // Constructor for the MessageLoop class, initializes all default values and creates an instance of the Algorithm class for each thread
        public MessageLoop(int i, String loop, String type)
        {
        	start = i;
        	length = 15;
        	if(pws == null)
        	{
        		pws = Collections.synchronizedSet(new HashSet<String>(40000000));
        	}
        	if(dpws == null)
        	{
        		dpws = Collections.synchronizedSet(new HashSet<String>());
        	}
        	A = "yahoo";
        	B = "xz62rP";
        	Loop = loop;
        	Type = type;
        	algInstance = new Algorithm(filename);
        }

		public void run() 
		{
			if(Loop.equals("threeLoop"))
			{
				threeLoop(Type);
			}
			else if(Loop.equals("iLoop"))
			{
				iLoop();
			}
			else if(Loop.equals("jLoop"))
			{
				jLoop();
			}
		}
		
		private void iLoop()
		{
			try
			{
				for (int i=start; i<30000000; i+=20)
				{
					String temp;
					temp = algInstance.getPW(filename, A, B, i, 0, length);
					
					if(!pws.add(temp))
					{
						System.out.println("duplicate found: "+ temp + " input was: (" + i + ")");
						dpws.add(temp);						
					}
					else
					{
						if(pws.size() % 10000 == 0)
						{
							System.out.println("added: " + temp + " there are now: " + pws.size() + " elements in set");
						}
					}
				}
				threadMessage("terminated with: " + Integer.valueOf(pws.size()).toString() + " in pws");
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		
		private void jLoop()
		{
			try
			{
				for (int j=start; j<30000000; j+=20)
				{
					String temp;
					temp = algInstance.getPW(filename, A, B, 0, j, length);
					
					if(!pws.add(temp))
					{
						System.out.println("duplicate found: "+ temp + " input was: (" + j + ")");
						dpws.add(temp);						
					}
					else
					{
						if(pws.size() % 10000 == 0)
						{
							System.out.println("added: " + temp + " there are now: " + pws.size() + " elements in set");
						}
					}
				}
				threadMessage("terminated with: " + Integer.valueOf(pws.size()).toString() + " in pws");
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		
		private void threeLoop(String type)
		{
			String[] test;
			
			if(type.equals("alpha"))
			{
				test = alphaTest;
			}
			else if(type.equals("ani"))
			{
				test = aniTest;
			}
			else
			{
				test = numTest;
			}
			
			try 
			{
                // Tries a set number of passwords and flags non-unique passwords
				// At the end of the loop, prints out the total number of unique passwords in the set for the terminated thread
				for (int i = start; i<1000; i += 20) 
                {
					for(int j=0; j<1000; j++)
					{
						for(String s : test)
						{
							String temp = algInstance.getPW(filename, A, s, i, j, length);
							if(!pws.add(temp))
							{
								System.out.println("duplicate found: "+ temp + " input was: (" + i + "," + j + "," + s + ")");
								dpws.add(temp);						
							}
							else
							{
								if(pws.size() % 10000 == 0)
								{
									System.out.println("added: " + temp + " there are now: " + pws.size() + " elements in set");
								}
							}
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
		filename = args[0];
		int seed = Integer.parseInt(args[1]);
		String loop = "";
		String type = "";
		try
		{
			loop = args[2];
		}
		catch(Exception e){ }
		try
		{
			type = args[3];
		}
		catch(Exception e){ }
		
		GenerateFile.seedGen(filename, symbolClass, 100, 100, seed);
		long start = System.nanoTime();
		
		// Create and start all the threads (20 in all)
		Thread a = new Thread(new MessageLoop(0,loop,type));
		Thread b = new Thread(new MessageLoop(1,loop,type));
		Thread c = new Thread(new MessageLoop(2,loop,type));
		Thread d = new Thread(new MessageLoop(3,loop,type));
		Thread e = new Thread(new MessageLoop(4,loop,type));
		Thread f = new Thread(new MessageLoop(5,loop,type));
		Thread g = new Thread(new MessageLoop(6,loop,type));
		Thread h = new Thread(new MessageLoop(7,loop,type));
		Thread i = new Thread(new MessageLoop(8,loop,type));
		Thread j = new Thread(new MessageLoop(9,loop,type));
		Thread k = new Thread(new MessageLoop(10,loop,type));
		Thread l = new Thread(new MessageLoop(11,loop,type));
		Thread m = new Thread(new MessageLoop(12,loop,type));
		Thread n = new Thread(new MessageLoop(13,loop,type));
		Thread o = new Thread(new MessageLoop(14,loop,type));
		Thread p = new Thread(new MessageLoop(15,loop,type));
		Thread q = new Thread(new MessageLoop(16,loop,type));
		Thread r = new Thread(new MessageLoop(17,loop,type));
		Thread s = new Thread(new MessageLoop(18,loop,type));
		Thread t = new Thread(new MessageLoop(19,loop,type));
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
		System.out.println("total number of duplicates: " + MessageLoop.dpws.size());
		System.out.println(getTime(end-start));
	}
}
