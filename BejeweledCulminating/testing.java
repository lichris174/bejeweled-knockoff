public class testing {
    public static void main (String[] args)
    {
        for (int j = 22; j <= 25; j++) {
            for (int k = 7; k <= 10; k++) {
                if (j % 2 == k % 2) {
                    System.out.println(j);
                } else {
                    System.out.println(k);
                }
            }
            System.out.println();
        }

    }
}
