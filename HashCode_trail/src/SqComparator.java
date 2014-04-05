import java.util.Comparator;


public class SqComparator implements Comparator<Coord> {
    @Override
    public int compare(Coord c1, Coord c2) {
    	if(c1.max<c2.max)
    		return 1;
    	else if(c1.max==c2.max)
    		return 0;
    	return -1;
    }
}
