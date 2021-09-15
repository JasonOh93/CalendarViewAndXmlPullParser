package com.jasonoh.calendarviewfromgoogle

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jasonoh.calendarviewfromgoogle.adapter.AnimalDataFromCityAdapter
import com.jasonoh.calendarviewfromgoogle.adapter.AnimalDataFromFullAdapter
import com.jasonoh.calendarviewfromgoogle.databinding.ActivityXmlPullParserBinding
import com.jasonoh.calendarviewfromgoogle.models.AnimalDataFromCity
import com.jasonoh.calendarviewfromgoogle.models.AnimalDataFull
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class XmlPullParserActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityXmlPullParserBinding
    private val binding get() = mBinding
//    var animalDatas = mutableListOf<AnimalDataFromCity>()
    var animalDatas = ArrayList<AnimalDataFromCity>()
    var animalDatasFull = ArrayList<AnimalDataFull>()
    lateinit var animalDataAdapter: AnimalDataFromCityAdapter
    lateinit var animalDataFullAdapter: AnimalDataFromFullAdapter

    private var page = 1
    private var loadingBoolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityXmlPullParserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnXmlAnimalCity.setOnClickListener {
            animalDatas.clear()
            page = 1
            loadingBoolean = false
            animalDataAdapter = AnimalDataFromCityAdapter(
                animalDatas,
                this@XmlPullParserActivity
            )
            binding.recyclerAnimal.adapter = animalDataAdapter
            animalDataAdapter.notifyDataSetChanged()

            loadAnimalDataSido()
        }

        binding.btnXmlAnimalFull.setOnClickListener {
            animalDatasFull.clear()
            page = 1
            loadingBoolean = false
            animalDataFullAdapter = AnimalDataFromFullAdapter(
                animalDatasFull,
                this@XmlPullParserActivity
            )
            binding.recyclerAnimal.adapter = animalDataFullAdapter
            animalDataFullAdapter.notifyDataSetChanged()

            loadAnimalDataFull()
        }

//        animalDataAdapter = AnimalDataFromCityAdapter(animalDatas, this)
//        animalDataFullAdapter = AnimalDataFromFullAdapter(animalDatasFull, this)
        binding.recyclerAnimal.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recyclerAnimal.addOnScrollListener(onScrollListener)
//        binding.recyclerAnimal.adapter = animalDataAdapter
//        animalDataAdapter.notifyDataSetChanged()




    }

    fun loadAnimalDataSido(){
        object : Thread() {
            override fun run() {
                super.run()

                val dataAnimalAddress: String =
                    "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/sido" +
                            "?serviceKey=" +
                            "6dqyQeM6Z1N4y9BZCEBwdt00gqLY6XZhny6jJs3ljEWE2NypmtrGJHRNkfgA%2FvtgZlWdqYCjoFGnPu3oKSTi0g%3D%3D" +
                            "&pageNo=" + page +
                            "&numOfRows=" + "10"

                try {
//                    animalDatas.clear()
                    val url: URL = URL(dataAnimalAddress)
                    val ist : InputStream = url.openStream()
                    val isr : InputStreamReader = InputStreamReader(ist)
                    val factory = XmlPullParserFactory.newInstance()
                    val xpp = factory.newPullParser()
                    xpp.setInput(isr)

                    var eventType = xpp.eventType

                    Log.e("TAG", "XmlPullParserActivity_run: ${eventType}", )

                    var stringBuffer = StringBuffer()

                    var cityCode: String? = ""
                    var cityName: String? = ""

                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        eventType = xpp.next()
                        when (eventType) {
                            XmlPullParser.START_DOCUMENT -> this@XmlPullParserActivity.runOnUiThread(Runnable {
                                Toast.makeText(
                                    this@XmlPullParserActivity,
                                    "검색시작",
                                    Toast.LENGTH_SHORT
                                ).show()
                            })
                            XmlPullParser.START_TAG -> {
                                val tagName_start = xpp.name
                                if (tagName_start == "items") stringBuffer =
                                    StringBuffer()
                                else if (tagName_start == "item") stringBuffer =
                                    StringBuffer()
                                else if (tagName_start == "orgCd") {
                                    xpp.next()
                                    cityCode = xpp.text
//                                    stringBuffer.append(xpp.text + "\n")
                                } else if (tagName_start == "orgdownNm") {
                                    xpp.next()
                                    cityName = xpp.text
                                    stringBuffer.append(xpp.text)
                                }
                            }
                            XmlPullParser.TEXT -> {
                            }
                            XmlPullParser.END_TAG -> if (xpp.name == "item") {
                                Log.w("TAG", "animal city : $stringBuffer")
                                animalDatas.add(
                                    AnimalDataFromCity(
                                        cityName!!,
                                        cityCode!!
                                    )
                                )
                                Log.w("TAG", "animal size: {${animalDatas[0].cityName}}")

                                stringBuffer = StringBuffer()
                            }
                        }
                    } //while

                    this@XmlPullParserActivity.runOnUiThread(Runnable {
                        if(cityName.equals("")) loadingBoolean = false
                        else loadingBoolean = true
                        Toast.makeText(this@XmlPullParserActivity, "검색종료 :: $cityName  :: $loadingBoolean", Toast.LENGTH_SHORT).show()

                        animalDataAdapter.notifyItemRangeInserted((page-1)*10, 10)

                        Log.w("TAG", "animal size: ${animalDatas.size}")

                        try {
                            isr.close()
                            ist.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    })


                }catch (exception: Exception){}

            }
        }.start()
    }

    fun loadAnimalDataFull(){
        object : Thread() {
            @SuppressLint("SimpleDateFormat")
            override fun run() {
                super.run()

                val cal = Calendar.getInstance()
                cal.time = Date()
                val dateFormat = SimpleDateFormat("yyyyMMdd")
                dateFormat.format(cal.time)
                val nowDate: String = dateFormat.format(cal.time)
//                일주일 단위로 검색이 안됨.. 이유가 뭘까???
//                8일 단위로는 검색이 된다..(오늘 날짜로 했을경우에만)
//                7일 단위는 하루 전날짜로 시도시 가능하다.
                cal.add(Calendar.DATE, -7)
                val aWeekAgoDate: String = dateFormat.format(cal.time)
                Log.e("TAG", "XmlPullParserActivity_run: afdfasfdasdfasd    ${aWeekAgoDate}  ${nowDate}", )

                val dataAnimalAddressFull: String =
                    "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic" +
                            "?bgnde=" + aWeekAgoDate +
                            "&endde=" + nowDate +
                            "&pageNo=" + page +
                            "&numOfRows=" + "10" +
                            "&state=" + "protect" +
                            "&serviceKey=" + "6dqyQeM6Z1N4y9BZCEBwdt00gqLY6XZhny6jJs3ljEWE2NypmtrGJHRNkfgA%2FvtgZlWdqYCjoFGnPu3oKSTi0g%3D%3D"

                try {
//                    animalDatas.clear()
                    val url: URL = URL(dataAnimalAddressFull)
                    val ist : InputStream = url.openStream()
                    val isr : InputStreamReader = InputStreamReader(ist)
                    val factory = XmlPullParserFactory.newInstance()
                    val xpp = factory.newPullParser()
                    xpp.setInput(isr)

                    var eventType = xpp.eventType

                    var stringBuffer = StringBuffer()

                    var age: String? = ""
                    var careAddr: String? = ""
                    var careNm: String? = ""
                    var careTel: String? = ""
                    var chargeNm: String? = ""
                    var colorCd: String? = ""
                    var desertionNo: String? = ""
                    var filename: String? = ""
                    var happenDt: String? = ""
                    var happenPlace: String? = ""
                    var kindCd: String? = ""
                    var neuterYn: String? = ""
                    var noticeEdt: String? = ""
                    var noticeNo: String? = ""
                    var noticeSdt: String? = ""
                    var officetel: String? = ""
                    var orgNm: String? = ""
                    var popfile: String? = ""
                    var processState: String? = ""
                    var sexCd: String? = ""
                    var specialMark: String? = ""
                    var weight: String? = ""


//                    Log.e("TAG", "XmlPullParserActivity_run: \n ${isr.readText()}", )

                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        eventType = xpp.next()
                        when (eventType) {
                            XmlPullParser.START_DOCUMENT -> this@XmlPullParserActivity.runOnUiThread(Runnable {
                                Toast.makeText(
                                    this@XmlPullParserActivity,
                                    "검색시작",
                                    Toast.LENGTH_SHORT
                                ).show()
                            })
                            XmlPullParser.START_TAG -> {
                                val tagName_start = xpp.name
                                if (tagName_start == "items") stringBuffer =
                                    StringBuffer()
                                else if (tagName_start == "item") stringBuffer =
                                    StringBuffer()
                                else if (tagName_start == "age") {
                                    xpp.next()
                                    age = xpp.text
                                    stringBuffer.append(xpp.text + "\n")
                                } else if (tagName_start == "careAddr") {
                                    xpp.next()
                                    careAddr = xpp.text
                                    stringBuffer.append(xpp.text + "\n")
                                } else if (tagName_start == "careNm") {
                                    xpp.next()
                                    careNm = xpp.text
                                    stringBuffer.append(xpp.text + "\n")
                                } else if (tagName_start == "careTel") {
                                    xpp.next()
                                    careTel = xpp.text
                                    stringBuffer.append(xpp.text + "\n")
                                } else if (tagName_start == "chargeNm") {
                                    xpp.next()
                                    chargeNm = xpp.text
                                    stringBuffer.append(xpp.text + "\n")
                                } else if (tagName_start == "colorCd") {
                                    xpp.next()
                                    colorCd = xpp.text
                                    stringBuffer.append(xpp.text + "\n")
                                } else if (tagName_start == "desertionNo") {
                                    xpp.next()
                                    desertionNo = xpp.text
                                    stringBuffer.append(xpp.text + "\n")
                                } else if (tagName_start == "filename") {
                                    xpp.next()
                                    filename = xpp.text
                                    stringBuffer.append(xpp.text + "\n")
                                } else if (tagName_start == "happenDt") {
                                    xpp.next()
                                    happenDt = xpp.text
                                    stringBuffer.append(xpp.text + "\n")
                                } else if (tagName_start == "happenPlace") {
                                    xpp.next()
                                    happenPlace = xpp.text
                                    stringBuffer.append(xpp.text + "\n")
                                } else if (tagName_start == "kindCd") {
                                    xpp.next()
                                    kindCd = xpp.text
                                    stringBuffer.append(xpp.text + "\n")
                                } else if (tagName_start == "neuterYn") {
                                    xpp.next()
                                    neuterYn = xpp.text
                                    stringBuffer.append(xpp.text + "\n")
                                } else if (tagName_start == "noticeEdt") {
                                    xpp.next()
                                    noticeEdt = xpp.text
                                    stringBuffer.append(xpp.text + "\n")
                                } else if (tagName_start == "noticeNo") {
                                    xpp.next()
                                    noticeNo = xpp.text
                                    stringBuffer.append(xpp.text + "\n")
                                } else if (tagName_start == "noticeSdt") {
                                    xpp.next()
                                    noticeSdt = xpp.text
                                    stringBuffer.append(xpp.text + "\n")
                                } else if (tagName_start == "officetel") {
                                    xpp.next()
                                    officetel = xpp.text
                                    stringBuffer.append(xpp.text + "\n")
                                } else if (tagName_start == "orgNm") {
                                    xpp.next()
                                    orgNm = xpp.text
                                    stringBuffer.append(xpp.text + "\n")
                                } else if (tagName_start == "popfile") {
                                    xpp.next()
                                    popfile = xpp.text
                                    stringBuffer.append(xpp.text + "\n")
                                } else if (tagName_start == "processState") {
                                    xpp.next()
                                    processState = xpp.text
                                    stringBuffer.append(xpp.text + "\n")
                                } else if (tagName_start == "sexCd") {
                                    xpp.next()
                                    sexCd = xpp.text
                                    stringBuffer.append(xpp.text + "\n")
                                } else if (tagName_start == "specialMark") {
                                    xpp.next()
                                    specialMark = xpp.text
                                    stringBuffer.append(xpp.text + "\n")
                                } else if (tagName_start == "weight") {
                                    xpp.next()
                                    weight = xpp.text
                                    stringBuffer.append(xpp.text + "\n")
                                }
                            }
                            XmlPullParser.TEXT -> {
                            }
                            XmlPullParser.END_TAG -> if (xpp.name == "item") {
                                Log.w("TAG", "animal city : $stringBuffer")
                                animalDatasFull.add(
                                    AnimalDataFull(
                                        age!!,
                                        careAddr!!,
                                        careNm!!,
                                        careTel!!,
                                        chargeNm!!,
                                        colorCd!!,
                                        desertionNo!!,
                                        filename!!,
                                        happenDt!!,
                                        happenPlace!!,
                                        kindCd!!,
                                        neuterYn!!,
                                        noticeEdt!!,
                                        noticeNo!!,
                                        noticeSdt!!,
                                        officetel!!,
                                        orgNm!!,
                                        popfile!!,
                                        processState!!,
                                        sexCd!!,
                                        specialMark!!,
                                        weight!!
                                    )
                                )
//                                Log.w("TAG", "animal size: {${animalDatas[0].cityName}}")

                                stringBuffer = StringBuffer()
                            }
                        }
                    } //while

                    this@XmlPullParserActivity.runOnUiThread(Runnable {
                        if(age.equals("")) loadingBoolean = false
                        else loadingBoolean = true
                        Toast.makeText(this@XmlPullParserActivity, "검색종료 :: $age", Toast.LENGTH_SHORT).show()

                        animalDataFullAdapter.notifyItemRangeInserted((page-1)*10, 10)

//                        Log.w("TAG", "animal size: ${animalDatas.size}")

                        try {
                            isr.close()
                            ist.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    })


                }catch (exception: Exception){}

            }
        }.start()
    }

    val onScrollListener: RecyclerView.OnScrollListener = object:RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

//          todo ::  findLastCompletelyVisibleItemPosition() -> 화면에 전부 보여야 해당 아이템 인덱스를 가져옴
            val firstVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findFirstCompletelyVisibleItemPosition() // 화면에 보이는 첫번째 아이템의 position
            val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() // 화면에 보이는 마지막 아이템의 position
            val itemTotalCount = recyclerView.adapter!!.itemCount - 1 // 어댑터에 등록된 아이템의 총 개수 -1


            Log.e("TAG", "XmlPullParserActivity_onScrolled: \n" +
                    "firstVisibleItemPosition = $firstVisibleItemPosition\nlastVisibleItemPosition = $lastVisibleItemPosition\nitemTotalCount = $itemTotalCount", )

            // 스크롤이 끝에 도달했는지 확인
//            https://thkim-study.tistory.com/14
//            https://todaycode.tistory.com/12
            if (!binding.recyclerAnimal.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount) {
//                Toast.makeText(this@XmlPullParserActivity, "끝에 도달", Toast.LENGTH_SHORT).show()
//                animalDatas.add(AnimalDataFromCity("seoul", "11123123"))
//                animalDataAdapter.notifyItemInserted(itemTotalCount+1)
                Log.e("TAG", "XmlPullParserActivity_onScrolled: 끝에 도달", )
                ++page
                if(recyclerView.adapter!!.javaClass.equals(AnimalDataFromFullAdapter::class.java) && loadingBoolean) loadAnimalDataFull()
                else if(recyclerView.adapter!!.javaClass.equals(AnimalDataFromCityAdapter::class.java) && loadingBoolean) loadAnimalDataSido()
            }
        }
    }
}