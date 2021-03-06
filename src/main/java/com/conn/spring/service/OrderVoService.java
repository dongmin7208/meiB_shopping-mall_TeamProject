package com.conn.spring.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.conn.spring.DAO.OrderVoDao;
import com.conn.spring.VO.OrderVO;

@Service
public class OrderVoService {
	@Autowired
	private OrderVoDao orderDao;
		
		public OrderVoDao getBoardDao() {
			return orderDao;
		}

		public void setBoardDao(OrderVoDao orderDao) {
			this.orderDao = orderDao;
		}
		
		public List<OrderVO> list(){
			return orderDao.list();
		}
		
		public int delete(OrderVO orderVO) {
				return orderDao.delete(orderVO);
		}
		
		public int edit(OrderVO orderVO) {
				return orderDao.update(orderVO);
		}
		public void write(OrderVO orderVO) {
				orderDao.insert(orderVO);
		}
		public OrderVO select(int PRONUM) {
				return orderDao.select(PRONUM);
		}
		
		@Service
		public class FileUploadService {
			private static final String SAVE_PATH = "/upload";
			private static final String PREFIX_URL = "/upload/";
			
			public String restore(MultipartFile multipartFile) {
				String url = null;
				
				try {
					// 파일 정보
					String originFilename = multipartFile.getOriginalFilename();
					String extName
						= originFilename.substring(originFilename.lastIndexOf("."), originFilename.length());
					Long size = multipartFile.getSize();
					
					// 서버에서 저장 할 파일 이름
					String saveFileName = genSaveFileName(extName);
					
					System.out.println("originFilename : " + originFilename);
					System.out.println("extensionName : " + extName);
					System.out.println("size : " + size);
					System.out.println("saveFileName : " + saveFileName);
					
					writeFile(multipartFile, saveFileName);
					url = PREFIX_URL + saveFileName;
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
				return url;
			}
			
			
			// 현재 시간을 기준으로 파일 이름 생성
			private String genSaveFileName(String extName) {
				String fileName = "";
				
				Calendar calendar = Calendar.getInstance();
				fileName += calendar.get(Calendar.YEAR);
				fileName += calendar.get(Calendar.MONTH);
				fileName += calendar.get(Calendar.DATE);
				fileName += calendar.get(Calendar.HOUR);
				fileName += calendar.get(Calendar.MINUTE);
				fileName += calendar.get(Calendar.SECOND);
				fileName += calendar.get(Calendar.MILLISECOND);
				fileName += extName;
				
				return fileName;
			}
			
			
			private boolean writeFile(MultipartFile multipartFile, String saveFileName)
										throws IOException{
				boolean result = false;

				byte[] data = multipartFile.getBytes();
				FileOutputStream fos = new FileOutputStream(SAVE_PATH + "/" + saveFileName);
				fos.write(data);
				fos.close();
				
				return result;
			}
		}

}
