# Report
Варіант операцій: V2 зі співвідношенням 2:100:30

Варіант сортування: S6 (сортування за датою народження)

Тестові набори даних: 100, 1000, 10000, 100000 студентів

### Операції V2:

Пошук студентів за днем народження (2 операції)
Зміна групи студента за email (100 операцій)
Пошук групи з найбільшою кількістю однакових днів народження (30 операцій)

### Реалізовані три типи баз даних:

ArrayListDatabase - простий список, мінімальна пам'ять (0.34MB для 100K студентів), але повільна робота (3.8K операцій за 10 сек)
HashEmailDatabase - з індексацією по email та днях народження, оптимальний баланс (5.5MB пам'яті, 4.5K операцій)
FullIndexDatabase - повна індексація, максимальна швидкість (12.6MB пам'яті, 38K операцій)

### Сортування S6:
Реалізовано два алгоритми сортування за датою народження:

Standard Sort (Java Collections.sort) - 25ms
Counting Sort (спеціалізований алгоритм) - 11ms - у 2.27 рази швидший

### Команди запуску

```bash
javac -d bin src/Main.java src/student/*.java src/student/impl/*.java src/student/sorter/*.java
java -cp bin Main
```

```bash
Loaded 100000 students

=== ArrayList Database ===
Testing with 100 students:
Memory usage: 0.00 MB
Operations completed - Op1: 257650, Op2: 12882289, Op3: 3868854, Total: 17008793
Testing with 1000 students:
Memory usage: 0.00 MB
Operations completed - Op1: 21013, Op2: 1050945, Op3: 315286, Total: 1387244
Testing with 10000 students:
Memory usage: 0.03 MB
Operations completed - Op1: 1756, Op2: 84924, Op3: 25418, Total: 112098
Testing with 100000 students:
Memory usage: 0.34 MB
Operations completed - Op1: 51, Op2: 2901, Op3: 870, Total: 3822

=== HashEmail Database ===
Testing with 100 students:
Memory usage: 0.02 MB
Operations completed - Op1: 267231, Op2: 13363573, Op3: 4012717, Total: 17643521
Testing with 1000 students:
Memory usage: 0.10 MB
Operations completed - Op1: 24261, Op2: 1215505, Op3: 364614, Total: 1604380
Testing with 10000 students:
Memory usage: 0.40 MB
Operations completed - Op1: 2552, Op2: 124507, Op3: 37446, Total: 164505
Testing with 100000 students:
Memory usage: 5.48 MB
Operations completed - Op1: 58, Op2: 3420, Op3: 1011, Total: 4489

=== FullIndex Database ===
Testing with 100 students:
Memory usage: 0.02 MB
Operations completed - Op1: 825690, Op2: 41224082, Op3: 12373530, Total: 54423302
Testing with 1000 students:
Memory usage: 0.08 MB
Operations completed - Op1: 380875, Op2: 19011677, Op3: 5707135, Total: 25099687
Testing with 10000 students:
Memory usage: 0.36 MB
Operations completed - Op1: 354791, Op2: 17706388, Op3: 5314417, Total: 23375596
Testing with 100000 students:
Memory usage: 12.64 MB
Operations completed - Op1: 596, Op2: 29118, Op3: 8671, Total: 38385

=== Sorting Performance (S6) ===
Standard sort time: 25 ms
Counting sort time: 11 ms
Sorting correctness: true
Sorted data saved to data/students_sorted.csv
```

### Аналіз графіків

Графік 1 показує загальну продуктивність: FullIndexDatabase значно переважає інші на всіх розмірах даних, але продуктивність різко падає зі зростанням кількості студентів.

Графік 2 демонструє використання пам'яті: чим складніша структура даних, тим більше пам'яті вона вимагає, причому FullIndexDatabase використовує майже в 40 разів більше пам'яті ніж ArrayListDatabase.

Графік 3 відображає ефективність використання пам'яті на одного студента: ArrayListDatabase найефективніший на великих данах (лише 0.0035KB на студента).

Графік 4 порівнює продуктивність по типах операцій: FullIndexDatabase переважає у всіх трьох операціях завдяки спеціальним індексам.

Графік 5 аналізує співвідношення продуктивність/пам'ять: HashEmailDatabase займає найкращу позицію з хорошим балансом між швидкістю та використанням пам'яті.

Графік 6 підтверджує ефективність Counting Sort для спеціалізованих типів даних.
