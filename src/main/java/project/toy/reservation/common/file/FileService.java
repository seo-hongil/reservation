package project.toy.reservation.common.file;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.toy.reservation.global.exception.BusinessException;
import project.toy.reservation.global.exception.ErrorCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class FileService {

    private final String uploadPath;

    public FileService(@Value("${file.uploadPath}") String uploadPath) {
        this.uploadPath = uploadPath;
    }

    @Transactional
    public List<String> uploadFiles(List<MultipartFile> files, String state, Long storeId){
        List<String> storedFilePaths = new ArrayList<>();
        String userPath = state + "/" + storeId;

        if(files == null || files.isEmpty()){
            return storedFilePaths;
        }

        for(MultipartFile file : files){
            if(file.isEmpty()) continue;

            String originalName = file.getOriginalFilename();
            String extension = originalName.substring(originalName.lastIndexOf("."));
            String uuid = UUID.randomUUID().toString();

            String savedName = uuid + extension;
            Path savePath = Paths.get(uploadPath, userPath, savedName);

            try{
                Files.createDirectories(savePath.getParent());
                file.transferTo(savePath.toFile());

                storedFilePaths.add(savedName);
            }catch (IOException e){
                log.error("파일 저장 실패: {}", originalName, e);
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "이미지 업로드 중 오류가 발생했습니다.");
            }
        }
        return storedFilePaths;
    }

    public void deleteFile(String fileName, String state, Long memberId) {
        String userPath = state + "/" + memberId;
        Path filePath = Paths.get(uploadPath, userPath, fileName);

        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("파일 삭제 성공: {}", fileName);
            }
        } catch (IOException e) {
            log.error("파일 삭제 실패: {}", fileName, e);
        }
    }
}
