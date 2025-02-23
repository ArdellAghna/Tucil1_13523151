# Tucil 1 IF2211 Strategi Algoritma

## IQ Puzzle Pro Solver
Program ini berfungsi untuk menyelesaikan permainan IQ Puzzler Pro menggunakan algoritma Brute Force murni. Program akan mencari dan memberikan satu solusi apabila seluruh papan permainan terisi dengan blok atau piece puzzle yang tersedia. Setiap blok dapat dirotasi maupun dicerminkan. Program ini akan hanya menerima input dalam bentuk file .txt.


## Data Diri
| NIM  | NAMA |
| ------------- | ------------- |
| 13523151 | Ardell Aghna Mahendra  |


## Struktur Program
```bash
Tucil1_13523151/
│
├── bin/
│
├── doc/
│    └── Tucil1_K3_13523151_Ardell Aghna Mahendra.pdf
│
├── src/
│    │
│    ├── Board.java
│    │
│    ├── GUI.java
│    │
│    ├── InputFile.java
│    │
│    ├── Main.java
│    │
│    ├── Piece.java
│    │
│    └── PuzzleSolver.java
│
├── test/
│    │
│    ├── Hasil_test1.png
│    │
│    ├── Hasil_test1.txt
│    │
│    ├── Hasil_test2.png
│    │
│    ├── Hasil_test2.txt
│    │
│    ├── Hasil_test3.png
│    │
│    ├── Hasil_test3.txt
│    │
│    ├── Hasil_test8.png
│    │
│    ├── Hasil_test8.txt
│    │
│    ├── Hasil_test9.png
│    │
│    ├── Hasil_test9.txt
│    │
│    ├── test1.txt
│    │
│    ├── test2.txt
│    │
│    ├── test3.txt
│    │
│    ├── test4.txt
│    │
│    ├── test5.txt
│    │
│    ├── test6.txt
│    │
│    ├── test7.txt
│    │
│    ├── test8.txt
│    │
│    └── test9.txt
│
└── README.md

```


## Bahasa yang Digunakan
Java

 
## Cara Menjalankan Program
1. Open github repository and copylink
   ```bash
   git clone https://github.com/ArdellAghna/Tucil1_13523151.git
   ```
2. Run command
    ```bash
    javac -d bin src/*.java
    ```
    ```bash
    java -cp bin Main
    ```


## Format Input
N & M : Dimensi papan

P : Jumlah blok/piece puzzle

S : Jenis kasus (DEFAULT/CUSTOM/PYRAMID)

Bentuk Puzzle : Huruf kapital A-Z

Contoh:

A

AA
```bash
N M P
S
puzzle_1_shape
puzzle_2_shape
…
puzzle_P_shape

```


## Link dan Bonus yang dikerjakan
Repository : https://github.com/ArdellAghna/Tucil1_13523151 

Bonus : GUI dan Output Gambar

Profile : https://github.com/ArdellAghna  
