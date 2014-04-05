import java.util.ArrayList;


public class Junction {
	int index;
	float x,y;
	int visitedTime;
	ArrayList<Integer> voisinList;//neighbours list
	ArrayList<Integer> stList;//streets that go out from this junction
	
	Junction(int i,float x,float y){
		this.x=x;
		this.y=y;
		this.index=i;
		this.visitedTime=0;
		voisinList=new ArrayList<Integer>();
		stList=new ArrayList<Integer>();
		}
	int sumLen(){
		int l=0;
		for(int st:stList){
			if(FinalRound.streets[st].visited==0)
				l+=FinalRound.streets[st].len;
		}
			return l;
	}
	int prosp(int st){//from st to this point how long can it go?
		int l=0;
		if(FinalRound.streets[st].visited==0)
			l+=FinalRound.streets[st].len;
		int maxl=0;
		for(int s:stList){
			if(s!=st && FinalRound.streets[st].visited==0 && FinalRound.streets[s].len>maxl)
				maxl=FinalRound.streets[s].len;
		}
		return l+maxl;
	}//prosp
	
	float dirModule(int dir){
		float d=0;
		switch(dir){
		case 0:
			d=-x+2*y;
			break;
		case 1:
			d=x;
			break;
		case 2:
			d=x+y;
			break;
		case 3:
			d=-x-2*y;
			break;
		case 4:
			d=-x+y;
			break;
		case 5:
			d=-x-y;
			break;
		case 6:
			d=-x+y;
			break;
		case 7:
			d=-x-y;
			break;
		}
		return d;
	}
}//class
