package com.skillproof.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy=Color(0xFF102A43)
private val Teal=Color(0xFF087F8C)
private val Mint=Color(0xFFE2F6F2)
private val Page=Color(0xFFF4F7FA)
private val Ink=Color(0xFF172B3A)
private val Muted=Color(0xFF667887)

data class Worker(val name:String,val trade:String,val country:String,val rating:String,val skills:List<String>,val initials:String,val evidence:String)
data class Vacancy(val id:Int,val title:String,val company:String,val country:String,val city:String,val type:String,val pay:String,val skills:List<String>,val posted:String,val description:String,val initials:String,val employerMarked:Boolean)
private val workers=listOf(
 Worker("Amina Rahman","Electrician","Bangladesh","4.9",listOf("Wiring","Safety","Repairs"),"AR","Portfolio submitted · references pending"),
 Worker("Daniel Okafor","Plumber","Nigeria","4.8",listOf("Pipe fitting","Leaks","Installation"),"DO","Work samples submitted · skill test available"),
 Worker("Maria Santos","Carpenter","Philippines","4.9",listOf("Furniture","Woodwork","Finishing"),"MS","Portfolio submitted · identity not verified"),
 Worker("Arjun Patel","Painter","India","4.7",listOf("Interior","Finishing","Repairs"),"AP","Work samples submitted · references pending")
)
private val vacancies=listOf(
 Vacancy(1,"Senior Electrician","Northstar Facilities","Bangladesh","Dhaka","Full-time","BDT 30k–45k / month",listOf("Electrical","Safety","3+ years"),"2 days ago","Install and maintain commercial electrical systems, troubleshoot faults and follow documented safety procedures. Applicants should provide work samples and relevant certificates.","NF",true),
 Vacancy(2,"Plumber — Building Maintenance","UrbanWorks Services","Bangladesh","Chattogram","Full-time","BDT 25k–35k / month",listOf("Plumbing","Maintenance","2+ years"),"5 hours ago","Handle plumbing repairs, pipe installation, leak diagnosis and preventive maintenance for residential and commercial sites.","UW",true),
 Vacancy(3,"Carpenter / Cabinet Maker","Craftline Studio","Philippines","Manila","Contract","PHP 700–1,100 / day",listOf("Carpentry","Woodwork","Finishing"),"1 day ago","Build and fit cabinets and furniture from technical drawings. A portfolio of completed projects is required.","CS",false),
 Vacancy(4,"Industrial Electrician","Kano Engineering Group","Nigeria","Lagos","Full-time","NGN 250k–380k / month",listOf("Industrial","Electrical","Troubleshooting"),"3 days ago","Maintain industrial electrical equipment, complete inspections and record repairs in line with site procedures.","KE",true),
 Vacancy(5,"Interior Painter","BrightSpace Projects","India","Bengaluru","Contract","INR 900–1,500 / day",listOf("Painting","Interior","Finishing"),"Yesterday","Prepare surfaces, apply interior coatings, protect finished areas and deliver consistent professional finishing.","BP",false),
 Vacancy(6,"Maintenance Technician","Global Habitat","UAE","Dubai","Full-time","AED 2,500–3,500 / month",listOf("Maintenance","Repairs","Multi-skill"),"4 days ago","Perform property maintenance and minor electrical, plumbing and finishing repairs. Confirm visa and contract terms independently.","GH",true)
)
class MainActivity:ComponentActivity(){override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContent{SkillProofApp()}}}

@Composable
fun SkillProofApp(){
 var tab by remember{mutableIntStateOf(0)}
 var search by remember{mutableStateOf("")}
 var country by remember{mutableStateOf("All")}
 var job by remember{mutableStateOf<Vacancy?>(null)}
 var worker by remember{mutableStateOf<Worker?>(null)}
 var saved by remember{mutableStateOf(setOf<Int>())}
 var applied by remember{mutableStateOf(setOf<Int>())}
 val jobs=vacancies.filter{(search.isBlank()||listOf(it.title,it.company,it.city,it.country,it.skills.joinToString()).any{v->v.contains(search,true)})&&(country=="All"||it.country==country)}
 val people=workers.filter{(search.isBlank()||listOf(it.name,it.trade,it.country,it.skills.joinToString()).any{v->v.contains(search,true)})&&(country=="All"||it.country==country)}
 MaterialTheme(colorScheme=lightColorScheme(primary=Teal,secondary=Navy,background=Page,surface=Color.White,onSurface=Ink)){
 Scaffold(containerColor=Page,bottomBar={NavigationBar(containerColor=Color.White){
  NavigationBarItem(tab==0,{tab=0;job=null;worker=null},icon={Icon(Icons.Default.Dashboard,null)},label={Text("Discover")})
  NavigationBarItem(tab==1,{tab=1;job=null;worker=null},icon={Icon(Icons.Default.WorkOutline,null)},label={Text("Vacancies")})
  NavigationBarItem(tab==2,{tab=2;job=null;worker=null},icon={Icon(Icons.Default.PeopleAlt,null)},label={Text("Workers")})
  NavigationBarItem(tab==3,{tab=3;job=null;worker=null},icon={Icon(Icons.Default.BookmarkBorder,null)},label={Text("Saved")})
 }}){padding->
  Column(Modifier.fillMaxSize().padding(padding)){
   if(job!=null) JobDetail(job!!,saved.contains(job!!.id),applied.contains(job!!.id),{job=null},{saved=if(saved.contains(job!!.id))saved-job!!.id else saved+job!!.id},{applied=applied+job!!.id})
   else if(worker!=null) WorkerDetail(worker!!,{worker=null})
   else when(tab){
    0->Discover(search,{search=it},jobs,people,{job=it;tab=1},{worker=it;tab=2},{tab=1})
    1->JobList(search,{search=it},country,{country=it},jobs,saved,applied,{job=it},{id->saved=if(saved.contains(id))saved-id else saved+id})
    2->WorkerList(search,{search=it},country,{country=it},people,{worker=it})
    else->SavedList(saved,applied,{job=it;tab=1},{saved=emptySet()})
   }
  }
 }}
}

@Composable private fun Header(subtitle:String){Row(Modifier.fillMaxWidth().padding(horizontal=20.dp,vertical=14.dp),verticalAlignment=Alignment.CenterVertically){
 Box(Modifier.size(44.dp).background(Teal,RoundedCornerShape(14.dp)),contentAlignment=Alignment.Center){Text("SP",color=Color.White,fontWeight=FontWeight.ExtraBold,fontSize=17.sp)}
 Spacer(Modifier.width(11.dp));Column(Modifier.weight(1f)){Text("SKILL PROOF",fontSize=12.sp,color=Teal,fontWeight=FontWeight.ExtraBold,letterSpacing=2.sp);Text(subtitle,fontSize=12.sp,color=Muted)}
 Surface(color=Mint,shape=RoundedCornerShape(50)){Row(Modifier.padding(horizontal=10.dp,vertical=7.dp),verticalAlignment=Alignment.CenterVertically){Icon(Icons.Default.Public,null,tint=Teal,modifier=Modifier.size(15.dp));Spacer(Modifier.width(5.dp));Text("Global",fontSize=12.sp,color=Teal,fontWeight=FontWeight.SemiBold)}}
}}
@Composable private fun SearchBox(value:String,change:(String)->Unit,hint:String){OutlinedTextField(value,change,Modifier.fillMaxWidth(),singleLine=true,shape=RoundedCornerShape(15.dp),placeholder={Text(hint,fontSize=13.sp)},leadingIcon={Icon(Icons.Default.Search,null,tint=Teal)},colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=Teal,unfocusedBorderColor=Color(0xFFDCE5EB),focusedContainerColor=Color.White,unfocusedContainerColor=Color.White))}
@Composable private fun Notice(){Surface(color=Color(0xFFFFF4DE),shape=RoundedCornerShape(12.dp)){Row(Modifier.fillMaxWidth().padding(12.dp),verticalAlignment=Alignment.Top){Icon(Icons.Default.Info,null,tint=Color(0xFF986314),modifier=Modifier.size(17.dp));Spacer(Modifier.width(8.dp));Text("Prototype sample data. Real employer verification, cloud sync and submitted applications are not connected.",fontSize=11.sp,lineHeight=16.sp,color=Color(0xFF76571F))}}}
@Composable private fun Discover(search:String,change:(String)->Unit,jobs:List<Vacancy>,people:List<Worker>,openJob:(Vacancy)->Unit,openWorker:(Worker)->Unit,seeJobs:()->Unit){
 LazyColumn(Modifier.fillMaxSize(),contentPadding=PaddingValues(bottom=22.dp),verticalArrangement=Arrangement.spacedBy(16.dp)){
  item{Header("A clearer way to hire skilled people")}
  item{Column(Modifier.padding(horizontal=18.dp),verticalArrangement=Arrangement.spacedBy(13.dp)){
   Card(shape=RoundedCornerShape(25.dp),colors=CardDefaults.cardColors(containerColor=Navy)){Column(Modifier.fillMaxWidth().padding(22.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
    Text("SKILLS MEET OPPORTUNITY",color=Color(0xFF7FE0CE),fontSize=10.sp,fontWeight=FontWeight.Bold,letterSpacing=1.sp)
    Text("Good work starts\nwith proof.",fontSize=30.sp,lineHeight=35.sp,fontWeight=FontWeight.ExtraBold,color=Color.White)
    Text("Explore jobs. Compare skills. Make informed hiring decisions.",fontSize=14.sp,color=Color(0xFFD2DFE8),lineHeight=21.sp)
    Button(onClick=seeJobs,shape=RoundedCornerShape(12.dp),colors=ButtonDefaults.buttonColors(containerColor=Color(0xFF5BD0BC),contentColor=Navy)){Text("Explore vacancies",fontWeight=FontWeight.Bold);Icon(Icons.Default.ArrowForward,null,modifier=Modifier.padding(start=6.dp).size(17.dp))}
   }}
   SearchBox(search,change,"Search jobs, skills or workers")
   Row(horizontalArrangement=Arrangement.spacedBy(10.dp)){Metric("Open roles",vacancies.size.toString(),"Sample listings",Modifier.weight(1f));Metric("Skill profiles",workers.size.toString(),"Sample profiles",Modifier.weight(1f))}
  }}
  item{Heading("Latest opportunities","View all",seeJobs)}
  items(jobs.take(3)){VacancyCard(it,false,false,{openJob(it)},{})}
  item{Heading("Worker profiles","Explore",{})}
  items(people.take(3)){WorkerCard(it,{openWorker(it)})}
  item{Notice()}
 }
}
@Composable private fun Metric(title:String,value:String,caption:String,modifier:Modifier){Card(modifier,shape=RoundedCornerShape(17.dp),colors=CardDefaults.cardColors(containerColor=Color.White)){Column(Modifier.padding(15.dp),verticalArrangement=Arrangement.spacedBy(4.dp)){Text(title,fontSize=12.sp,color=Muted);Text(value,fontSize=25.sp,color=Navy,fontWeight=FontWeight.ExtraBold);Text(caption,fontSize=10.sp,color=Teal)}}}
@Composable private fun Heading(title:String,action:String,click:()->Unit){Row(Modifier.fillMaxWidth().padding(horizontal=18.dp),verticalAlignment=Alignment.CenterVertically){Text(title,Modifier.weight(1f),fontSize=18.sp,fontWeight=FontWeight.Bold,color=Navy);Text(action,color=Teal,fontWeight=FontWeight.Bold,fontSize=12.sp,modifier=Modifier.clickable{click()})}}
@Composable private fun JobList(search:String,change:(String)->Unit,country:String,setCountry:(String)->Unit,jobs:List<Vacancy>,saved:Set<Int>,applied:Set<Int>,open:(Vacancy)->Unit,toggle:(Int)->Unit){
 Column(Modifier.fillMaxSize()){Header("Find work that fits your skills");Column(Modifier.padding(horizontal=16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){Text("Vacancies",fontSize=27.sp,fontWeight=FontWeight.ExtraBold,color=Navy);Text("Browse roles by trade, location and work type.",fontSize=13.sp,color=Muted);SearchBox(search,change,"Job title, company, skill or city");Row(horizontalArrangement=Arrangement.spacedBy(5.dp)){listOf("All","Bangladesh","Nigeria","Philippines","India","UAE").forEach{FilterChip(country==it,{setCountry(it)},{Text(it,fontSize=10.sp)},shape=RoundedCornerShape(10.dp))}};Text(jobs.size.toString()+" opportunities",fontSize=12.sp,color=Muted)}
 LazyColumn(Modifier.weight(1f),contentPadding=PaddingValues(16.dp),verticalArrangement=Arrangement.spacedBy(11.dp)){items(jobs){VacancyCard(it,saved.contains(it.id),applied.contains(it.id),{open(it)},{toggle(it.id)})};if(jobs.isEmpty())item{Empty("No vacancies found","Try a different keyword or country.")};item{Notice()}}
 }}
@Composable private fun VacancyCard(j:Vacancy,isSaved:Boolean,isApplied:Boolean,open:()->Unit,toggle:()->Unit){
 Card(Modifier.fillMaxWidth().clickable{open()},shape=RoundedCornerShape(19.dp),colors=CardDefaults.cardColors(containerColor=Color.White),border=BorderStroke(1.dp,Color(0xFFE6EDF1))){Column(Modifier.padding(15.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
  Row(verticalAlignment=Alignment.Top){Box(Modifier.size(45.dp).background(Mint,RoundedCornerShape(13.dp)),contentAlignment=Alignment.Center){Text(j.initials,color=Teal,fontWeight=FontWeight.ExtraBold)};Spacer(Modifier.width(10.dp));Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(3.dp)){Text(j.title,fontWeight=FontWeight.Bold,fontSize=15.sp,color=Navy,lineHeight=19.sp);Row(verticalAlignment=Alignment.CenterVertically){Text(j.company,fontSize=12.sp,color=Muted);if(j.employerMarked)Icon(Icons.Default.Verified,null,tint=Teal,modifier=Modifier.padding(start=4.dp).size(14.dp))};Text(j.city+", "+j.country,fontSize=12.sp,color=Muted)};IconButton(onClick=toggle,modifier=Modifier.size(32.dp)){Icon(if(isSaved)Icons.Default.Bookmark else Icons.Default.BookmarkBorder,null,tint=Teal)}}
  Text(j.pay,color=Teal,fontSize=14.sp,fontWeight=FontWeight.Bold)
  Row(horizontalArrangement=Arrangement.spacedBy(5.dp)){j.skills.take(3).forEach{Surface(color=Page,shape=RoundedCornerShape(7.dp)){Text(it,Modifier.padding(horizontal=7.dp,vertical=5.dp),fontSize=10.sp,color=Ink)}}}
  Row(verticalAlignment=Alignment.CenterVertically){Text(j.type+" · "+j.posted,Modifier.weight(1f),fontSize=10.sp,color=Muted);Text(if(isApplied)"Applied · demo" else "View details →",fontSize=11.sp,color=Teal,fontWeight=FontWeight.Bold)}
 }}
}
@Composable private fun JobDetail(j:Vacancy,saved:Boolean,applied:Boolean,back:()->Unit,toggle:()->Unit,apply:()->Unit){
 Column(Modifier.fillMaxSize()){Row(Modifier.padding(horizontal=12.dp,vertical=8.dp),verticalAlignment=Alignment.CenterVertically){IconButton(onClick=back){Icon(Icons.Default.ArrowBack,null)};Text("Vacancy details",fontSize=18.sp,fontWeight=FontWeight.Bold,color=Navy)}
 Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal=17.dp),verticalArrangement=Arrangement.spacedBy(15.dp)){
  Card(shape=RoundedCornerShape(24.dp),colors=CardDefaults.cardColors(containerColor=Navy)){Column(Modifier.fillMaxWidth().padding(21.dp),verticalArrangement=Arrangement.spacedBy(11.dp)){Box(Modifier.size(54.dp).background(Color(0xFF24465E),RoundedCornerShape(15.dp)),contentAlignment=Alignment.Center){Text(j.initials,color=Color.White,fontWeight=FontWeight.ExtraBold,fontSize=18.sp)};if(j.employerMarked)Text("Employer marked verified · sample data",color=Color(0xFF7FE0CE),fontSize=11.sp);Text(j.title,fontSize=25.sp,lineHeight=30.sp,color=Color.White,fontWeight=FontWeight.ExtraBold);Text(j.company+" · "+j.city+", "+j.country,color=Color(0xFFD2DFE8),fontSize=13.sp);Text(j.pay,color=Color(0xFF7FE0CE),fontSize=17.sp,fontWeight=FontWeight.Bold)}}
  DetailSection("About this role",j.description)
  Text("Skills requested",fontSize=17.sp,color=Navy,fontWeight=FontWeight.Bold)
  Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){j.skills.forEach{Surface(color=Mint,shape=RoundedCornerShape(8.dp)){Text(it,Modifier.padding(horizontal=9.dp,vertical=7.dp),color=Teal,fontSize=11.sp,fontWeight=FontWeight.SemiBold)}}}
  DetailSection("Before applying","Confirm employer identity, pay, working hours, contract and any visa requirements independently. Do not send money before checking the legitimacy of a recruiter.")
  Notice()
 }
 Row(Modifier.fillMaxWidth().background(Color.White).padding(13.dp),horizontalArrangement=Arrangement.spacedBy(9.dp)){OutlinedButton(onClick=toggle,modifier=Modifier.weight(0.8f).height(50.dp),shape=RoundedCornerShape(13.dp)){Icon(if(saved)Icons.Default.Bookmark else Icons.Default.BookmarkBorder,null);Text(if(saved)"Saved" else "Save",modifier=Modifier.padding(start=5.dp))};Button(onClick=apply,enabled=!applied,modifier=Modifier.weight(1.2f).height(50.dp),shape=RoundedCornerShape(13.dp),colors=ButtonDefaults.buttonColors(containerColor=Teal)){Text(if(applied)"Applied (demo)" else "Apply now",fontWeight=FontWeight.Bold)}}
 }}
@Composable private fun DetailSection(title:String,body:String){Column(verticalArrangement=Arrangement.spacedBy(7.dp)){Text(title,fontSize=17.sp,fontWeight=FontWeight.Bold,color=Navy);Text(body,fontSize=14.sp,lineHeight=22.sp,color=Muted)}}
@Composable private fun WorkerList(search:String,change:(String)->Unit,country:String,setCountry:(String)->Unit,people:List<Worker>,open:(Worker)->Unit){
 Column(Modifier.fillMaxSize()){Header("Explore people and practical skills");Column(Modifier.padding(horizontal=16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){Text("Worker directory",fontSize=26.sp,fontWeight=FontWeight.ExtraBold,color=Navy);SearchBox(search,change,"Name, trade or skill");Row(horizontalArrangement=Arrangement.spacedBy(5.dp)){listOf("All","Bangladesh","Nigeria","Philippines","India").forEach{FilterChip(country==it,{setCountry(it)},{Text(it,fontSize=10.sp)},shape=RoundedCornerShape(10.dp))}};Text(people.size.toString()+" profiles",fontSize=12.sp,color=Muted)}
 LazyColumn(Modifier.weight(1f),contentPadding=PaddingValues(16.dp),verticalArrangement=Arrangement.spacedBy(11.dp)){items(people){WorkerCard(it,{open(it)})};if(people.isEmpty())item{Empty("No profiles found","Try another trade or country.")};item{Notice()}}
 }}
@Composable private fun WorkerCard(w:Worker,open:()->Unit){Card(Modifier.fillMaxWidth().clickable{open()},shape=RoundedCornerShape(18.dp),colors=CardDefaults.cardColors(containerColor=Color.White),border=BorderStroke(1.dp,Color(0xFFE6EDF1))){Column(Modifier.padding(15.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){Row(verticalAlignment=Alignment.CenterVertically){Box(Modifier.size(46.dp).background(Mint,CircleShape),contentAlignment=Alignment.Center){Text(w.initials,color=Teal,fontWeight=FontWeight.ExtraBold)};Spacer(Modifier.width(10.dp));Column(Modifier.weight(1f)){Text(w.name,fontWeight=FontWeight.Bold,color=Navy,fontSize=15.sp);Text(w.trade+" · "+w.country,fontSize=12.sp,color=Muted)};Text("★ "+w.rating,color=Color(0xFF996300),fontWeight=FontWeight.Bold,fontSize=12.sp)};Row(horizontalArrangement=Arrangement.spacedBy(5.dp)){w.skills.forEach{Surface(color=Page,shape=RoundedCornerShape(7.dp)){Text(it,Modifier.padding(horizontal=7.dp,vertical=5.dp),fontSize=10.sp,color=Ink)}}};Text(w.evidence,fontSize=11.sp,color=Muted);Text("View skill profile →",fontSize=12.sp,color=Teal,fontWeight=FontWeight.Bold)}}}
@Composable private fun WorkerDetail(w:Worker,back:()->Unit){Column(Modifier.fillMaxSize()){Row(Modifier.padding(horizontal=12.dp,vertical=8.dp),verticalAlignment=Alignment.CenterVertically){IconButton(onClick=back){Icon(Icons.Default.ArrowBack,null)};Text("Worker profile",fontSize=18.sp,fontWeight=FontWeight.Bold,color=Navy)};Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(14.dp)){Box(Modifier.size(86.dp).background(Mint,CircleShape),contentAlignment=Alignment.Center){Text(w.initials,color=Teal,fontSize=26.sp,fontWeight=FontWeight.ExtraBold)};Text(w.name,fontSize=24.sp,color=Navy,fontWeight=FontWeight.ExtraBold);Text(w.trade+" · "+w.country,color=Muted);Text("★ "+w.rating+" · sample rating",color=Color(0xFF996300),fontWeight=FontWeight.Bold);DetailSection("Skills",w.skills.joinToString(" · "));DetailSection("Evidence status",w.evidence);Notice()}}}
@Composable private fun SavedList(saved:Set<Int>,applied:Set<Int>,open:(Vacancy)->Unit,clear:()->Unit){val list=vacancies.filter{saved.contains(it.id)};Column(Modifier.fillMaxSize()){Header("Keep useful opportunities close");Row(Modifier.fillMaxWidth().padding(horizontal=17.dp),verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text("Saved jobs",fontSize=26.sp,fontWeight=FontWeight.ExtraBold,color=Navy);Text(list.size.toString()+" saved in this session",fontSize=12.sp,color=Muted)};if(list.isNotEmpty())Text("Clear all",modifier=Modifier.clickable{clear()},fontSize=12.sp,color=Teal,fontWeight=FontWeight.Bold)};LazyColumn(Modifier.fillMaxSize(),contentPadding=PaddingValues(16.dp),verticalArrangement=Arrangement.spacedBy(11.dp)){items(list){VacancyCard(it,true,applied.contains(it.id),{open(it)},{})};if(list.isEmpty())item{Empty("Nothing saved yet","Open a vacancy and tap the bookmark.")};item{Notice()}}}}
@Composable private fun Empty(title:String,body:String){Column(Modifier.fillMaxWidth().padding(vertical=35.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(8.dp)){Icon(Icons.Default.SearchOff,null,tint=Teal,modifier=Modifier.size(40.dp));Text(title,fontWeight=FontWeight.Bold,fontSize=18.sp,color=Navy);Text(body,fontSize=13.sp,color=Muted)}}
