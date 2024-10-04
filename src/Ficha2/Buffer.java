package src.Ficha2;

public class Buffer {

    //volatile = Speaks to computer that this variable will be [read* and wrote*] for many threads
    //volatile = So don't put this on cache.
    public volatile static int [] buffer;
    public volatile static boolean [] isOccupied;

    public volatile static int bufferSize;
    //threads producers needs knows this
    public volatile static int min;
    public volatile static int max;
}
