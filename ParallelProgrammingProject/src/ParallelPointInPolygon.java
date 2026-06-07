import java.util.Random;

public class ParallelPointInPolygon {

    // Nokta sınıfı (X ve Y koordinatları)
    static class Point {
        double x, y;
        boolean isInside;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
            this.isInside = false;
        }
    }

    // Thread İşçisi Sınıfı
    static class WorkerThread extends Thread {
        private final Point[] points;
        private final Point[] polygon;
        private final int startIndex;
        private final int endIndex;

        public WorkerThread(Point[] points, Point[] polygon, int startIndex, int endIndex) {
            this.points = points;
            this.polygon = polygon;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        @Override
        public void run() {
            for (int i = startIndex; i < endIndex; i++) {
                points[i].isInside = rayCastingInsidePolygon(points[i], polygon);
            }
        }
    }

    public static void main(String[] args) {
        // --- PARAMETRELER ---
        int numPoints = 5000000; // 5 Milyon Nokta (Farklı nokta sayıları için değiştirebiliriz)
        System.out.println("==================================================");
        System.out.println("    PARALEL PROGRAMLAMA PROJE TEST MERKEZİ        ");
        System.out.println("    Test Edilen Toplam Nokta Sayısı: " + numPoints);
        System.out.println("==================================================");

        // --- 1. GEOMETRİK VERİLERİN HAZIRLANMASI ---
        Point[] polygon = {
            new Point(10.0, 10.0),
            new Point(90.0, 10.0),
            new Point(90.0, 90.0),
            new Point(50.0, 120.0), // Konkav/Konveks geçişi sağlayan tepe noktası
            new Point(10.0, 90.0)
        };

        Point[] pointsTemplate = new Point[numPoints];
        Random rand = new Random(42); // Her testte aynı noktalar üretilsin diye sabit seed
        for (int i = 0; i < numPoints; i++) {
            pointsTemplate[i] = new Point(rand.nextDouble() * 150.0, rand.nextDouble() * 150.0);
        }

        // --- 2. ARDIŞIL (SEQUENTIAL - 1 THREAD) ÇÖZÜM ---
        Point[] pointsSeq = clonePoints(pointsTemplate);
        long startTimeSeq = System.currentTimeMillis();
        
        for (int i = 0; i < numPoints; i++) {
            pointsSeq[i].isInside = rayCastingInsidePolygon(pointsSeq[i], polygon);
        }
        
        long endTimeSeq = System.currentTimeMillis();
        long durationSeq = endTimeSeq - startTimeSeq;
        System.out.printf("%-25s : %d ms\n", "Ardışıl (1 Thread) Süre", durationSeq);
        System.out.println("--------------------------------------------------");

        // --- 3. FARKLI THREAD SAYILARI İLE PARALEL TESTLER ---
        // Bilgisayarındaki çekirdeklere göre (2, 4, 8, 16 vb.) otomatik test eder
        int maxThreads = Runtime.getRuntime().availableProcessors();
        int[] threadConfigs = {2, 4, 8, 16}; 

        for (int numThreads : threadConfigs) {
            
            Point[] pointsPar = clonePoints(pointsTemplate);
            long startTimePar = System.currentTimeMillis();

            Thread[] threads = new Thread[numThreads];
            int chunk = numPoints / numThreads;

            for (int i = 0; i < numThreads; i++) {
                int start = i * chunk;
                int end = (i == numThreads - 1) ? numPoints : (start + chunk);
                threads[i] = new WorkerThread(pointsPar, polygon, start, end);
                threads[i].start();
            }

            try {
                for (int i = 0; i < numThreads; i++) {
                    threads[i].join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long endTimePar = System.currentTimeMillis();
            long durationPar = endTimePar - startTimePar;
            double speedup = (double) durationSeq / durationPar;

            System.out.printf("Paralel (%2d Thread) Süre  : %d ms  |  Hızlanma (Speedup): %.2fx\n", 
                              numThreads, durationPar, speedup);
        }
        System.out.println("==================================================");
    }

    // Yardımcı Metot: Nokta dizisini her test için sıfırlamak üzere kopyalar
    private static Point[] clonePoints(Point[] src) {
        Point[] dest = new Point[src.length];
        for (int i = 0; i < src.length; i++) {
            dest[i] = new Point(src[i].x, src[i].y);
        }
        return dest;
    }

    // Ray-Casting (Işın Salma) Algoritması
    public static boolean rayCastingInsidePolygon(Point p, Point[] polygon) {
        boolean inside = false;
        int n = polygon.length;
        for (int i = 0, j = n - 1; i < n; j = i++) {
            if (((polygon[i].y > p.y) != (polygon[j].y > p.y)) &&
                (p.x < (polygon[j].x - polygon[i].x) * (p.y - polygon[i].y) / (polygon[j].y - polygon[i].y) + polygon[i].x)) {
                inside = !inside;
            }
        }
        return inside;
    }
}