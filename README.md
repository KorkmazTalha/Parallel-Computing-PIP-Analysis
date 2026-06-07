# 📽️ Proje Video Sunumu
Bu projenin detaylı anlatımı ve çalışma mantığına ait video sunumuna **[Buraya Tıklayarak (YouTube)](https://youtu.be/X6iWgDuafhM)** ulaşabilirsiniz.

# 📍 Paralel Point in Polygon (PIP) Tespiti

Bu proje, Java Çoklu Thread (Multi-threading) mimarisini kullanarak, milyonlarca rastgele noktanın belirli bir poligonun içinde olup olmadığını **Ray-Casting Algoritması** ile tespit eden yüksek performanslı bir uygulamadır.

## 🚀 Öne Çıkan Özellikler
- **Embarrassingly Parallel Uygulama:** Veri bloklama (chunking) yöntemiyle çok çekirdekli işlemciler için optimize edilmiştir.
- **Ölçeklenebilirlik Analizi:** **1M, 5M ve 10M nokta** setleri ile kapsamlı testler yapılmıştır.
- **Yüksek Verimlilik:** 16 thread kullanımında **6.00 kata kadar** hızlanma (speedup) elde edilmiştir.
- **Thread Güvenliği:** Karşılaştırmalı testler için nokta klonlama yöntemiyle izole bellek yönetimi sağlanmıştır.

## 📊 Performans Ölçümleri
| Veri Seti | Ardışıl (1 Thread) | Paralel (8 Thread) | Maksimum Hızlanma |
| :--- | :--- | :--- | :--- |
| 1 Milyon | 30 ms | 7 ms | 6.00x |
| 5 Milyon | 138 ms | 33 ms | 4.18x |
| 10 Milyon | 255 ms | 70 ms | 4.72x |

## 🛠️ Teknoloji Yığını
- **Dil:** Java 17+
- **Eşzamanlılık:** Java Threads (Worker Pattern)
- **Algoritma:** Ray-Casting (Hesaplamalı Geometri)

## 💡 Teknik Çıkarımlar
Proje, **Amdahl Yasası**'nın pratikteki yansımasını göstermektedir. Performans, fiziksel çekirdek doygunluğuna kadar lineer artış gösterirken, 5M noktalı test setinde görüldüğü üzere doygunluk noktasından sonra **Context Switching (Bağlam Değişimi)** maliyeti görünür hale gelmektedir.
