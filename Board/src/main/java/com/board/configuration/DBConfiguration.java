package com.board.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration // @Configuration: @Configuration이 지정된 클래스를 자바 기반의 설정 파일로 인식
@PropertySource("classpath:/application.properties") // @propertySource: 해당 클래스에서 참조할 properties 파일의 위치 지정
public class DBConfiguration {

	@Autowired // @Autowired: Bean으로 등록된 인스턴스(이하 객체)를 클래스에 주입하는 데 사용
	private ApplicationContext applicationContext; // ApplicationContext는 스프링 컨테이너 중 하나! Bean의 생성과 사용, 관계, 생명 주기 등을 관리

	@Bean // @Configuration 클래스의 메소드 레벨에서만 지정이 가능하며, @Bean이 지정된 객체는 컨테이너에 의해 관리되는 Bean으로 등록됨
	@ConfigurationProperties(prefix = "spring.datasource.hikari") // @PropertySource에 지정된 파일(application.properties)에서 prefix에 해당하는 spring.datasource.hikari로 시작하는 설정을 모두 읽어 들여 해당 메소드에 매핑함
	public HikariConfig hikariConfig() { // hikariConfig: 히카리CP 객체를 생성. 히카리CP는 커넥션 풀 라이브러리 중 하나.
		return new HikariConfig();
	}

	@Bean
	public DataSource dataSource() { // dataSource: 데이터 소스 객체를 생성. 데이터 소스는 커넥션 풀을 지원하기 위한 인터페이스 (커넥션풀: 커넥션 객체를 생성해두고, 디비에 접근하는 사용자에게 미리 생성해둔 커넥션을 제공했다가 다시 돌려받는 방법)
		return new HikariDataSource(hikariConfig());
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception { // SqlSessionFactory는 디비의 커넥션과 SQL 실행에 대한 모든 것을 갖는 정말 중요한 역할을 함
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean(); // SqlSessionFactoryBean: 마이바티스와 스프링의 연동 모듈로 사용
		factoryBean.setDataSource(dataSource());
		factoryBean.setMapperLocations(applicationContext.getResources("classpath:/mappers/**/*Mapper.xml")); // getResources 메서드의 인자로 지정된 패턴에 포함되는 XMl Mapper를 인식하도록 하는 역할을 함
		factoryBean.setTypeAliasesPackage("com.board.domain");
		factoryBean.setConfiguration(mybatisConfg()); // 마이바티스 설정과 관련된 빈을 설정 파일로 지정
		return factoryBean.getObject();
	}

	@Bean
	public SqlSessionTemplate sqlSession() throws Exception { // SqlSessionTemplate 마이바티스 스프링 연동 모듈의 핵심. SqlSession을 구현하고, 코드에서 sqlSession을 대체하는 역할을 함. 쓰레드에 안전하고, 여러 개의 DAO나 Mapper에서 공유할 수 있다. 필요한 시점에 세션을 닫고, 커밋 또는 롤백하는 것을 포함한 세션의 생명주기를 관리함.
		return new SqlSessionTemplate(sqlSessionFactory());
	}
	
	@Bean
	@ConfigurationProperties(prefix = "mybatis.configuration")
	public org.apache.ibatis.session.Configuration mybatisConfg() {
		return new org.apache.ibatis.session.Configuration();
	}

}