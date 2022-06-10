/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ktpdua.crudktp;

import dummy.Dummy;
import dummy.exceptions.NonexistentEntityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author laela
 */
@Controller
public class DataController {
    
    DataJpaController datactrl = new DataJpaController();
    List<Data> newdata = new ArrayList<>();
    
    @RequestMapping("/main")
    public String getMain(){
        return "menu";
    }
    
    @RequestMapping("/data")
    //@ResponseBody
    public String getDataKTP(Model model){
        int record = datactrl.getDataCount();
        String result="";
        try {
           newdata = datactrl.findDataEntities().subList(0, record);
        }
        catch (Exception e){result=e.getMessage();}
        model.addAttribute("goData", newdata);
        model.addAttribute("record", record);
        
        return "database";
    }
    
    @RequestMapping("/buat")
    public String createDummy() {
        return "buat";
    }

    @PostMapping(value = "/newdata", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public RedirectView newDummyData(@RequestParam("foto") MultipartFile f, HttpServletRequest r)
            throws ParseException, Exception {
        Data d = new Data();

        long id = Integer.parseInt(r.getParameter("id"));
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(r.getParameter("tgllahir"));
        byte[] img = f.getBytes();
        
        String agama = r.getParameter("agama");
        String alamat = r.getParameter("alamat");
        String berlaku = r.getParameter("berlakuhingga");
        String jk = r.getParameter("jeniskelamin");
        String nama = r.getParameter("nama");
        String noktp = r.getParameter("noktp");
        String pekerjaan = r.getParameter("pekerjaan");
        String status = r.getParameter("status");
        
        String warganegara = r.getParameter("warganegara");
        d.setId(id);
        d.setAgama(agama);
        d.setAlamat(alamat);
        d.setBerlakuhingga(berlaku);
        d.setFoto(img);
        d.setJeniskelamin(jk);
        d.setNama(nama);
        d.setNoktp(noktp);
        d.setPekerjaan(pekerjaan);
        d.setStatus(status);
        d.setTgllahir(date);
        d.setWarganegara(warganegara);
        

        datactrl.create(d);
        return new RedirectView("/data");
    }
    
    

    @RequestMapping(value = "/img", method = RequestMethod.GET, produces = {
        MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE
    })
    public ResponseEntity<byte[]> getImg(@RequestParam("id") long id) throws Exception {
        Data d = datactrl.findData(id);
        byte[] img = d.getFoto();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(img);
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public String deleteDummy(@PathVariable("id") long id) throws Exception {
        datactrl.destroy(id);
        return "deleted";
    }

    @GetMapping("/edit/{id}")
    public String updateDummy(@PathVariable(value = "id") long id, Model m) throws NonexistentEntityException {
        Data d = datactrl.findData(id);
        
        m.addAttribute("data", d);
        return "edit";
    }

    @PostMapping(value = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    
    public RedirectView updateDummyData(@RequestParam("foto") MultipartFile f, HttpServletRequest r)
            throws ParseException, Exception {
        Data d = new Data();

        long id = Integer.parseInt(r.getParameter("id"));
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(r.getParameter("tgllahir"));
        byte[] img = f.getBytes();
        
        String agama = r.getParameter("agama");
        String alamat = r.getParameter("alamat");
        String berlaku = "Seumur Hidup";
        String jk = r.getParameter("jeniskelamin");
        String nama = r.getParameter("nama");
        String noktp = r.getParameter("noktp");
        String pekerjaan = r.getParameter("pekerjaan");
        String status = r.getParameter("status");
        
        String warganegara = r.getParameter("warganegara");
        d.setId(id);
        d.setAgama(agama);
        d.setAlamat(alamat);
        d.setBerlakuhingga(berlaku);
        d.setFoto(img);
        d.setJeniskelamin(jk);
        d.setNama(nama);
        d.setNoktp(noktp);
        d.setPekerjaan(pekerjaan);
        d.setStatus(status);
        d.setTgllahir(date);
        d.setWarganegara(warganegara);

        datactrl.edit(d);
        return new RedirectView("/data");
    }
}

