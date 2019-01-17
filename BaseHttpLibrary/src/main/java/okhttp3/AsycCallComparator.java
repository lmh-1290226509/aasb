package okhttp3;

import java.util.Comparator;

public class AsycCallComparator<T> implements Comparator<T> {

    @Override
    public int compare(T object1, T object2) {

        if ((object1 instanceof RealCall.AsyncCall)
                && (object2 instanceof RealCall.AsyncCall)) {

            RealCall.AsyncCall task1 = (RealCall.AsyncCall) object1;
            RealCall.AsyncCall task2 = (RealCall.AsyncCall) object2;

            int result = task2.priority()
                    - task1.priority();

            return result;
        }
        return 0;
    }
}
