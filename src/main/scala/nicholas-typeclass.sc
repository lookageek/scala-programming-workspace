type Cell = String
type Row  = List[Cell]
type Csv  = List[Row]

def parseCsv(data: String): Csv =
  data.
    split("\n").toList.
    map(_.split(",").toList)

val input = """1,2,3
4,5,6
7,8,9"""

parseCsv(input)

//res0.head.head