package com.skillproof.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Green = Color(0xFF176B55)
private val Ink = Color(0xFF14231F)
private val Mint = Color(0xFFE6F4ED)
private val Canvas = Color(0xFFF6F8F5)
private val Muted = Color(0xFF68756E)

data class Worker(val name:String,val role:String,val country:String,val rating:String,val jobs:Int,val skills:List<String>,val initials:String,val status:String)
private val sample = listOf(
 Worker("Amina Rahman","Electrician","Bangladesh","4.9",38,listOf("Wiring","Safety","Repairs"),"AR","Portfolio submitted · References not checked"),
 Worker("Daniel Okafor","Plumber","Nigeria","4.8",26,listOf("Pipe fitting","Leaks","Installation"),"DO","Work samples submitted · Skill test available"),
 Worker("Maria Santos","Carpenter","Philippines","4.9",51,listOf("Furniture","Woodwork","Finishing"),"MS","Portfolio submitted · Identity not verified"),
 Worker("Arjun Patel","Painter","India","4.7",19,listOf("Interior","Finishing","Repairs"),"AP","Work samples submitted · References pending")
)

class MainActivity:ComponentActivity(){ override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContent { App() }} }

@Composable fun App(){
 var query by remember { mutableStateOf("") }; var country by remember { mutableStateOf("All countries") }; var detail by remember { mutableStateOf<Worker?>(null) }
 val countries=listOf("All countries","Bangladesh","Nigeria","Philippines","India")
 val data=sample.filter{(query.isBlank()||it.name.contains(query,true)||it.role.contains(query,true)||it.skills.any{s->s.contains(query,true)})&&(country=="All countries"||it.country==country)}
 MaterialTheme(colorScheme=lightColorScheme(primary=Green,background=Canvas,surface=Color.White,onSurface=Ink)){
 if(detail!=null){Column(Modifier.fillMaxSize().background(Canvas).padding(20.dp),verticalArrangement=Arrangement.spacedBy(14.dp)){
 Text("‹ Back",color=Green,fontWeight=FontWeight.Bold,modifier=Modifier.clickable{detail=null})
 Card(shape=RoundedCornerShape(24.dp)){Column(Modifier.fillMaxWidth().padding(24.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(12.dp)){
 Box(Modifier.size(82.dp).background(Mint,CircleShape),contentAlignment=Alignment.Center){Text(detail!!.initials,color=Green,fontSize=26.sp,fontWeight=FontWeight.Bold)}
 Text(detail!!.name,fontSize=24.sp,fontWeight=FontWeight.Bold);Text("${detail!!.role} · ${detail!!.country}",color=Muted);Text("★ ${detail!!.rating} · ${detail!!.jobs} reviews/jobs",color=Green)
 HorizontalDivider();Text("Skills & evidence",fontWeight=FontWeight.Bold,fontSize=18.sp)
 detail!!.skills.forEach{Text("•  $it",modifier=Modifier.fillMaxWidth())}
 Text(detail!!.status,color=Muted);Text("Demo profile only. No independent verification has been performed.",color=Color(0xFF946515),fontSize=12.sp)
 }}
 }}else{LazyColumn(Modifier.fillMaxSize().background(Canvas),contentPadding=PaddingValues(18.dp),verticalArrangement=Arrangement.spacedBy(14.dp)){
 item{Row(verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text("SKILL PROOF",color=Green,fontSize=12.sp,fontWeight=FontWeight.Bold,letterSpacing=2.sp);Text("Hire with confidence.",fontSize=28.sp,fontWeight=FontWeight.Bold);Text("Find skills. Review evidence. Choose wisely.",color=Muted,fontSize=13.sp)};Box(Modifier.size(48.dp).background(Green,RoundedCornerShape(15.dp)),contentAlignment=Alignment.Center){Text("SP",color=Color.White,fontWeight=FontWeight.ExtraBold,fontSize=18.sp)}}}
 item{Card(shape=RoundedCornerShape(22.dp),colors=CardDefaults.cardColors(containerColor=Ink)){Column(Modifier.padding(20.dp),verticalArrangement=Arrangement.spacedBy(8.dp)){Text("GOOD WORK STARTS WITH TRUST",color=Color(0xFF9EE2C5),fontSize=11.sp,fontWeight=FontWeight.Bold,letterSpacing=1.sp);Text("Skills you can explore.",color=Color.White,fontSize=23.sp,fontWeight=FontWeight.Bold);Text("Compare portfolios, ratings and check status before hiring.",color=Color(0xFFD5E1DC))}}}
 item{OutlinedTextField(query,{query=it},Modifier.fillMaxWidth(),placeholder={Text("Search workers or skills")},leadingIcon={Icon(Icons.Default.Search,null)},singleLine=true,shape=RoundedCornerShape(15.dp))}
 item{Text("Browse by country",fontWeight=FontWeight.Bold,fontSize=18.sp);Column{Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){countries.take(3).forEach{FilterChip(country==it,{country=it},{Text(it)})}};Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){countries.drop(3).forEach{FilterChip(country==it,{country=it},{Text(it)})}}}}
 item{Text("Workers to explore · ${data.size}",fontWeight=FontWeight.Bold,fontSize=20.sp)}
 items(data){w->Card(Modifier.fillMaxWidth().clickable{detail=w},shape=RoundedCornerShape(21.dp),colors=CardDefaults.cardColors(containerColor=Color.White)){Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(11.dp)){
 Row(verticalAlignment=Alignment.CenterVertically){Box(Modifier.size(52.dp).background(Mint,CircleShape),contentAlignment=Alignment.Center){Text(w.initials,color=Green,fontWeight=FontWeight.Bold,fontSize=18.sp)};Spacer(Modifier.width(10.dp));Column(Modifier.weight(1f)){Text(w.name,fontWeight=FontWeight.Bold,fontSize=16.sp);Text("${w.role} · ${w.country}",color=Muted,fontSize=12.sp)};Text("★ ${w.rating}",color=Color(0xFF9A6B12),fontWeight=FontWeight.Bold)}
 Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){w.skills.forEach{Surface(color=Mint,shape=RoundedCornerShape(8.dp)){Text(it,color=Green,fontSize=11.sp,modifier=Modifier.padding(horizontal=8.dp,vertical=5.dp))}}}
 Text(w.status,color=Muted,fontSize=12.sp);Row(verticalAlignment=Alignment.CenterVertically){Text("View profile",color=Green,fontWeight=FontWeight.Bold,fontSize=13.sp);Icon(Icons.Default.ArrowForward,null,tint=Green,modifier=Modifier.size(16.dp))}
 }}}
 if(data.isEmpty())item{Text("No matches. Try another skill or country.",color=Muted)}
 item{Text("DEMO PROTOTYPE · Sample profiles only. No identity or background checks have been performed.",color=Muted,fontSize=11.sp)}
 }}
 }
}
