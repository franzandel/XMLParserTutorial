package com.tunaikumobile.xmlparsertutorial

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        parseXML()
    }

    private fun parseXML() {
        val parserFactory: XmlPullParserFactory
        try {
            parserFactory = XmlPullParserFactory.newInstance()
            val parser = parserFactory.newPullParser()
            val dataInputStream = assets.open("data.xml")

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(dataInputStream, null)

            processParsing(parser)

        } catch (e: XmlPullParserException) {
            Log.d("1234", e.message.toString())
        } catch (e: IOException) {
        }

    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun processParsing(parser: XmlPullParser) {
        val players = ArrayList<Players>()
        var eventType = parser.eventType
        var currentPlayers: Players? = null

        while (eventType != XmlPullParser.END_DOCUMENT) {
            var eltName: String?

            when (eventType) {
                XmlPullParser.START_TAG -> {
                    eltName = parser.name

                    if ("player" == eltName) {
                        currentPlayers = Players()
                        players.add(currentPlayers)
                    } else if (currentPlayers != null) {
                        when (eltName) {
                            "name" -> currentPlayers.name = parser.nextText()
                            "age" -> currentPlayers.age = parser.nextText()
                            "position" -> currentPlayers.position = parser.nextText()
                        }
                    }
                }
            }

            eventType = parser.next()
        }

        printPlayers(players)
    }

    private fun printPlayers(players: ArrayList<Players>) {
        val builder = StringBuilder()

        for (player in players) {
            builder.append(player.name).append("\n").append(player.age).append("\n")
                .append(player.position).append("\n\n")
        }

        txt.text = builder.toString()
    }
}
