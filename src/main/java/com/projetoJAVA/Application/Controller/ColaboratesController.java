package com.projetoJAVA.Application.Controller;

import com.projetoJAVA.Application.Business.ColaboratesBusiness;
import com.projetoJAVA.Application.ChefeDTO.Chefe_SubordinadoDTO;
import com.projetoJAVA.Application.colaboratesEntity.Colaborates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController()
@RequestMapping("/colaborates")
@CrossOrigin("*")

public class ColaboratesController {

    @Autowired
    public ColaboratesBusiness colaboratesBusiness;

    @GetMapping("/{id}")
    public Colaborates get(@PathVariable Integer id) {
        return colaboratesBusiness.findById(id);
    }

    @GetMapping
    public List<Colaborates> get() {
        return colaboratesBusiness.findAll();
    }

    @PostMapping
    public Colaborates post(@RequestBody Colaborates colaborates) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return colaboratesBusiness.save(colaborates);
    }

    @PostMapping("/Associar")
    public ResponseEntity<?> associar(@RequestBody Chefe_SubordinadoDTO chefe_subordinadoDTO) {
        if (chefe_subordinadoDTO.getIdChefe() == null || chefe_subordinadoDTO.getIdSubordinado() == null) {
            return ResponseEntity.badRequest().body("IDs de chefe e subordinado são obrigatórios.");
        }

        try {
            Colaborates colaborates = colaboratesBusiness.associar(chefe_subordinadoDTO);
            return ResponseEntity.ok(colaborates);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
