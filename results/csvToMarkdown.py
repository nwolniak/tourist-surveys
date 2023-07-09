import json

fileName = "2023-07-08-18-50-06.json"

f = open(fileName, encoding="utf8")
data = json.load(f)

with open(fileName.split('.')[0]+".html", 'w', encoding="utf8") as outputFile:

  outputFile.write("<html><head><meta charset='utf-8'></head><body><center>")
  outputFile.write('<style> table, th, td { border:1px solid black;}</style>')

  for i in data:
      outputFile.write('<table> <tr> <th>Question</th> <th>Answer</th> </tr>')
      outputFile.write("<h1>" + i['fileName'] + "</h1>")
      for j in i['surveyList']:
          outputFile.write(
              "<tr> <td>" + j['question'] + "</td> <td>" + j['answer'] + "</td> </tr>")
      outputFile.write("</table>")
      outputFile.write("<hr>")
  outputFile.write("</center></body></html>")

f.close()
