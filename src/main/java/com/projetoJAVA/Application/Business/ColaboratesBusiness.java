package com.projetoJAVA.Application.Business;

import com.projetoJAVA.Application.ChefeDTO.Chefe_SubordinadoDTO;
import com.projetoJAVA.Application.repository.IColaborates;
import com.projetoJAVA.Application.colaboratesEntity.Colaborates;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;


@Service
public class ColaboratesBusiness{

    @Autowired
    public IColaborates ColaboratesRepository;

    private static final String PASSWORD = "senha123";
    private static final String SALT = "salt123";
    private static final int KEY_LENGTH = 128;

    public Colaborates findById (Integer id){
        return ColaboratesRepository.findById(id).get();
    }

    public List<Colaborates> findAll() {
        return ColaboratesRepository.findAll();
    }

    public Colaborates save(Colaborates colaborates) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        colaborates.setScore(checkPasswordStrength(colaborates.getSenha()));
        colaborates.setSenha(cript(colaborates.getSenha()));
        return ColaboratesRepository.save(colaborates);
    }

    public Colaborates associar(@NotNull Chefe_SubordinadoDTO chefe_subordinadoDTO) {

        Colaborates chefe = ColaboratesRepository.findById(chefe_subordinadoDTO.getIdChefe())
                .orElseThrow(() -> new IllegalArgumentException("Chefe não encontrado para o ID: " + chefe_subordinadoDTO.getIdChefe()));

        Colaborates subordinado = ColaboratesRepository.findById(chefe_subordinadoDTO.getIdSubordinado())
                .orElseThrow(() -> new IllegalArgumentException("Subordinado não encontrado para o ID: " + chefe_subordinadoDTO.getIdSubordinado()));

        subordinado.setChefe(chefe);
        ColaboratesRepository.save(subordinado);

        return subordinado;
    }

    public String checkPasswordStrength(String pwd) {

        String ALPHAS = "abcdefghijklmnopqrstuvwxyz";
        String NUMERICS = "01234567890";
        String SYMBOLS = ")!@#$%^&*()";
        int MIN_PWD_LEN = 8;

        int nScore = 0, nLength = 0, nAlphaUC = 0, nAlphaLC = 0, nNumber = 0, nSymbol = 0, nMidChar = 0;
        int nRepChar = 0, nRepInc = 0, nConsecAlphaUC = 0, nConsecAlphaLC = 0, nConsecNumber = 0, nConsecSymbol = 0;
        int nSeqAlpha = 0, nSeqNumber = 0, nSeqSymbol = 0, nRequirements = 0;
        String sAlphaUC = "0", sAlphaLC = "0", sNumber = "0", sSymbol = "0", sMidChar = "0", sRequirements = "0";

        if (pwd != null && !pwd.isEmpty()) {
            nLength = pwd.length();
            nScore = nLength * 4;

            String[] arrPwd = pwd.replaceAll("\\s+", "").split("");
            int arrPwdLen = arrPwd.length;

            String nTmpAlphaUC = "", nTmpAlphaLC = "", nTmpNumber = "", nTmpSymbol = "";

            for (int a = 0; a < arrPwdLen; a++) {
                if (arrPwd[a].matches("[A-Z]")) {
                    if (!nTmpAlphaUC.equals("") && Integer.parseInt(nTmpAlphaUC) + 1 == a) {
                        nConsecAlphaUC++;
                    }
                    nTmpAlphaUC = Integer.toString(a);
                    nAlphaUC++;
                } else if (arrPwd[a].matches("[a-z]")) {
                    if (!nTmpAlphaLC.equals("") && Integer.parseInt(nTmpAlphaLC) + 1 == a) {
                        nConsecAlphaLC++;
                    }
                    nTmpAlphaLC = Integer.toString(a);
                    nAlphaLC++;
                } else if (arrPwd[a].matches("[0-9]")) {
                    if (a > 0 && a < (arrPwdLen - 1)) {
                        nMidChar++;
                    }
                    if (!nTmpNumber.equals("") && Integer.parseInt(nTmpNumber) + 1 == a) {
                        nConsecNumber++;
                    }
                    nTmpNumber = Integer.toString(a);
                    nNumber++;
                } else if (arrPwd[a].matches("[^a-zA-Z0-9_]")) {
                    if (a > 0 && a < (arrPwdLen - 1)) {
                        nMidChar++;
                    }
                    if (!nTmpSymbol.equals("") && Integer.parseInt(nTmpSymbol) + 1 == a) {
                        nConsecSymbol++;
                    }
                    nTmpSymbol = Integer.toString(a);
                    nSymbol++;
                }

                // Check for repeated characters (if same char appears more than once)
                for (int b = 0; b < arrPwdLen; b++) {
                    if (arrPwd[a].equals(arrPwd[b]) && a != b) {
                        nRepChar++;
                    }
                }
            }

            // Check for sequential patterns
            for (int s = 0; s < 23; s++) {
                String sFwd = ALPHAS.substring(s, Math.min(s + 3, ALPHAS.length()));
                String sRev = new StringBuilder(sFwd).reverse().toString();
                if (pwd.toLowerCase().contains(sFwd) || pwd.toLowerCase().contains(sRev)) {
                    nSeqAlpha++;
                }
            }

            for (int s = 0; s < 8; s++) {
                String sFwd = NUMERICS.substring(s, Math.min(s + 3, NUMERICS.length()));
                String sRev = new StringBuilder(sFwd).reverse().toString();
                if (pwd.toLowerCase().contains(sFwd) || pwd.toLowerCase().contains(sRev)) {
                    nSeqNumber++;
                    nScore -= 4;  // Deduct less for sequences
                }
            }

            for (int s = 0; s < 8; s++) {
                String sFwd = SYMBOLS.substring(s, Math.min(s + 3, SYMBOLS.length()));
                String sRev = new StringBuilder(sFwd).reverse().toString();
                if (pwd.toLowerCase().contains(sFwd) || pwd.toLowerCase().contains(sRev)) {
                    nSeqSymbol++;
                }
            }

            // Apply bonuses
            if (nAlphaUC > 0) {
                nScore += (nLength - nAlphaUC) * 2;
            }
            if (nAlphaLC > 0) {
                nScore += (nLength - nAlphaLC) * 2;
            }
            if (nNumber > 0) {
                nScore += nNumber * 4;
            }
            if (nSymbol > 0) {
                nScore += nSymbol * 6;
            }
            if (nMidChar > 0) {
                nScore += nMidChar * 2;
            }

            // Apply deductions
            if (nAlphaLC > 0 && nAlphaUC == 0 && nSymbol == 0 && nNumber == 0) {
                nScore -= nLength;
            }
            if (nAlphaLC == 0 && nAlphaUC == 0 && nSymbol == 0 && nNumber > 0) {
                nScore -= nLength;
            }
            if (nRepChar > 0) {
                nScore -= nRepInc;
            }
            if (nConsecAlphaUC > 0) {
                nScore -= nConsecAlphaUC * 2;
            }
            if (nConsecAlphaLC > 0) {
                nScore -= nConsecAlphaLC * 2;
            }
            if (nConsecNumber > 0) {
                nScore -= nConsecNumber * 2;
            }
            if (nSeqAlpha > 0) {
                nScore -= nSeqAlpha * 3;
            }
            if (nSeqNumber > 0) {
                nScore -= nSeqNumber * 3;
            }
            if (nSeqSymbol > 0) {
                nScore -= nSeqSymbol * 3;
            }

            // Set complexity level
            String complexity = "Too Short";
            if (nScore >= 0 && nScore < 20) {
                complexity = "Very Weak";
            } else if (nScore >= 20 && nScore < 40) {
                complexity = "Weak";
            } else if (nScore >= 40 && nScore < 60) {
                complexity = "Good";
            } else if (nScore >= 60 && nScore < 80) {
                complexity = "Strong";
            } else if (nScore >= 80 && nScore <= 100) {
                complexity = "Very Strong";
            }

        }
        return(nScore + "%");
    }

    public String cript(String password) {
        try {
            //Derivar a chave usando PBKDF2
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(PASSWORD.toCharArray(), SALT.getBytes(), 10000, KEY_LENGTH);
            SecretKeySpec secretKeySpec = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] encryptedbytes = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedbytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

