package com.example.hesapmakinesi

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    private var yeniIslemeBaslandi = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textResult = findViewById<TextView>(R.id.TextResult)

        val sayilar = listOf(
            R.id.Number0, R.id.Number1, R.id.Number2, R.id.Number3,
            R.id.Number4, R.id.Number5, R.id.Number6, R.id.Number7,
            R.id.Number8, R.id.Number9
        ).map { findViewById<Button>(it) }

        sayilar.forEach { btn ->
            btn.setOnClickListener {
                val current = textResult.text.toString()
                val value = btn.text.toString()

                // "=" sonrası sayı girildiyse ekran temizlenir
                if (yeniIslemeBaslandi || current == "0" || current == "Hata") {
                    textResult.text = value
                    yeniIslemeBaslandi = false
                } else {
                    textResult.append(value)
                }
            }
        }

        val toplama = findViewById<Button>(R.id.Toplama)
        val carpma = findViewById<Button>(R.id.Carpma)
        val bolme = findViewById<Button>(R.id.Bölme)
        val cikarma = findViewById<Button>(R.id.Cıkarma)
        val yuzde = findViewById<Button>(R.id.Yüzde)
        val parantez = findViewById<Button>(R.id.Parentez)
        val pnalma = findViewById<Button>(R.id.PNdeger)
        val esittir = findViewById<Button>(R.id.Esittir)
        val delete = findViewById<Button>(R.id.Clean)
        val ondalik = findViewById<Button>(R.id.Ondalık)

        val backspace = findViewById<Button>(R.id.Delete)


        fun appendOperator(op: String) {
            val current = textResult.text.toString()
            if (current.isNotEmpty() && current.last() !in "+-*/%".toCharArray() && current != "Hata") {
                textResult.append(op)
                yeniIslemeBaslandi = false
            }
        }


        backspace.setOnClickListener {
            val current = textResult.text.toString()
            if (current.isNotEmpty() && current != "0" && current != "Hata") {
                val newText = current.dropLast(1)
                textResult.text = if (newText.isEmpty()) "0" else newText
            } else {
                textResult.text = "0"
            }
        }


        toplama.setOnClickListener { appendOperator("+") }
        carpma.setOnClickListener { appendOperator("*") }
        bolme.setOnClickListener { appendOperator("/") }
        cikarma.setOnClickListener { appendOperator("-") }
        yuzde.setOnClickListener { appendOperator("%") }
        ondalik.setOnClickListener { appendOperator(".") }

        var parantezAcik = true
        parantez.setOnClickListener {
            textResult.append(if (parantezAcik) "(" else ")")
            parantezAcik = !parantezAcik
        }

        pnalma.setOnClickListener {
            val current = textResult.text.toString()
            if (current.startsWith("-")) textResult.text = current.substring(1)
            else textResult.text = "-$current"
        }

        delete.setOnClickListener {
            textResult.text = "0"
            yeniIslemeBaslandi = false
        }

        esittir.setOnClickListener {
            try {
                val expr = textResult.text.toString()
                val result = evaluate(expr)

                val sonucMetin = if (result % 1.0 == 0.0) {
                    result.toInt().toString()
                } else {
                    result.toString()
                }

                textResult.text = sonucMetin
                yeniIslemeBaslandi = true

            } catch (e: Exception) {
                textResult.text = "Hata"
                yeniIslemeBaslandi = true
            }
        }

        ondalik.setOnClickListener {
            val current = textResult.text.toString()
            if (!current.endsWith(".") && !current.split(Regex("[+\\-*/%()]")).last().contains(".")) {
                textResult.append(".")
            }
        }

    }

    private fun evaluate(expression: String): Double {
        var expr = expression.replace("×", "*").replace("÷", "/")

        val regex = Regex("(\\d+(?:\\.\\d+)?)%(\\d+(?:\\.\\d+)?)")
        expr = expr.replace(regex) {
            val a = it.groupValues[1].toDouble()
            val b = it.groupValues[2].toDouble()
            "(${a}*${b}/100)"
        }

        val result = try {
            object {
                var pos = -1
                var ch = 0
                fun nextChar() { pos++; ch = if (pos < expr.length) expr[pos].code else -1 }
                fun eat(charToEat: Int): Boolean {
                    while (ch == ' '.code) nextChar()
                    if (ch == charToEat) { nextChar(); return true }
                    return false
                }
                fun parse(): Double {
                    nextChar()
                    val x = parseExpression()
                    if (pos < expr.length) throw RuntimeException("Unexpected: ${ch.toChar()}")
                    return x
                }
                fun parseExpression(): Double {
                    var x = parseTerm()
                    while (true) {
                        when {
                            eat('+'.code) -> x += parseTerm()
                            eat('-'.code) -> x -= parseTerm()
                            else -> return x
                        }
                    }
                }
                fun parseTerm(): Double {
                    var x = parseFactor()
                    while (true) {
                        when {
                            eat('*'.code) -> x *= parseFactor()
                            eat('/'.code) -> x /= parseFactor()
                            else -> return x
                        }
                    }
                }
                fun parseFactor(): Double {
                    if (eat('+'.code)) return parseFactor()
                    if (eat('-'.code)) return -parseFactor()
                    var x: Double
                    val startPos = pos
                    if (eat('('.code)) {
                        x = parseExpression()
                        eat(')'.code)
                    } else if ((ch in '0'.code..'9'.code) || ch == '.'.code) {
                        while ((ch in '0'.code..'9'.code) || ch == '.'.code) nextChar()
                        x = expr.substring(startPos, pos).toDouble()
                    } else throw RuntimeException("Unexpected: ${ch.toChar()}")
                    if (eat('^'.code)) x = x.pow(parseFactor())
                    return x
                }
            }.parse()
        } catch (e: Exception) {
            Double.NaN
        }

        return if (result.isNaN() || result.isInfinite()) 0.0 else result
    }

}
