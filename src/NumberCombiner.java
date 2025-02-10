public class NumberCombiner {

    private long combinedValue = 0;
    
    public NumberCombiner(int... content) {
        for (int i : content) {
            add(i);
        }
    }

    // 存储数字的方法
    public void add(int number)
    {
        if (number > 63) throw new IllegalArgumentException("Number must be between 0 and 63");
        // 将对应位设置为 1
        combinedValue |= 1L << number;
    }

    // 检查数字是否被存储的方法
    public boolean contains(int number)
    {
        if (number > 63) return false;
        // 检查对应位是否为 1
        return (combinedValue & (1L << number)) != 0;
    }

    // 移除数字的方法
    public void remove(int number)
    {
        if (number > 63) return;
        // 将对应位设置为 0
        combinedValue &= ~(1L << number);
    }
}
