import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.LinkedList;

import tc.TC;

public class Trail {
	static boolean[][] img;
	static boolean[][] colored;
	static int r,c;
	static LinkedList<Coord> blackList=new LinkedList<Coord>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TC.lectureDansFichier("doodle.txt");
		TC.ecritureDansNouveauFichier("out.txt");
		String s = TC.lireLigne();
		r = Integer.parseInt(TC.motsDeChaine(s)[0]);
		c = Integer.parseInt(TC.motsDeChaine(s)[1]);
		img = new boolean[r][c];
		colored = new boolean[r][c];
		
		int i = 0, j = 0;
		for (i = 0; i < r; i++) {
			s = TC.lireLigne();
			for (j = 0; j < c; j++) {
				if(s.charAt(j) == '#'){
					img[i][j] = true;
					blackList.add(new Coord(i,j));//add to blackList
				}
				else
					img[i][j] = false;
				colored[i][j]=false;
			}
		}
		System.out.println("data read! " + r + " " + c + " ");
		// /////////////////////////////////////////////////////
		
		System.out.println("5 5 1==>"+isSameColor(5, 5, 1));

		for(Coord coord:blackList){
			coord.max=findMax(coord.x, coord.y);
		}
		Collections.sort(blackList, new SqComparator());//sorted!!
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("list.data"));
			oos.writeObject(blackList);
//			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("list.data"));
//			timetable = (String[][]) ois.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		int n=0;
		for(Coord coord:blackList){
			if(colored[coord.x][coord.y]==false){
				TC.println("PAINTSQ " + coord.x + " " + coord.y + " "+coord.max);
				n++;
				System.out.println(coord.max);
				changeSq(coord.x,coord.y,coord.max);
				}
		}
		
//		int n=printCmd();
		System.out.println(n);
		

		
	}//main

	static int isSameColor(int x, int y, int size) {//0 if same color, else nb of xx
		if(size==0)
			return 0;
		else if(size>x||size>y||size+x>=r||size+y>=c)
			return -1;
		boolean b =  img[x][y];
		int n=0;
		for(int i=0;i<size*2+1;i++){
			for(int j=0;j<size*2+1;j++){
				if(img[x-size+i][y-size+j]!=b)
					n++;
				}
		}
		return n;
	}//isSameColor
	
	static void changeSq(int x, int y, int size) {// change the colored[][]

		for (int i = 0; i < size * 2 + 1; i++) {
			for (int j = 0; j < size * 2 + 1; j++) {
				colored[x-size+i][y-size+j] = true;
			}
		}

	}// changeSq
	
	static int printCmd(){
		int i,j,n=0;
		for (i = 0; i < r; i++) {
			for (j = 0; j < c; j++) {
				if (colored[i][j]==false && img[i][j] == true) {
					TC.println("PAINTSQ " + i + " " + j + " 0");
					n++;
				}
			}// j
		}// i
		return n;
	}//printCmd
	
	static int findMax(int x,int y){//find max size centered in (x,y)
		int size=0;
		while(isSameColor(x,y,size)==0){
			size++;
		}
		return size-1;
	}
	
	
	
}//class
