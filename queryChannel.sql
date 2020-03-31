CREATE OR REPLACE procedure printName
as
cursor c_name is
	select ename from emp;
	
	temp_name emp.ename%TYPE;
	temp_sal emp.SAL%TYPE;
	
begin
	
	open c_name;
	LOOP
	fetch c_name
		into temp_name;
		
		select count(*) into temp_sal from emp e where e.ENAME = '111';
		if temp_sal>0 then
			select e.SAL into temp_sal from emp e where e.ENAME = '111';
		end if; 
		
		  IF SQL%FOUND THEN     --使用隐式游标SQL%FOUND属性，判断数据是否修改成功
				DBMS_OUTPUT.PUT_LINE('成功修改雇员工资！');
				EXIT when SQL%NOTFOUND;
			END IF;
			
		EXIT when c_name%NOTFOUND; -- loop循环结束条件
	DBMS_OUTPUT.PUT_LINE(temp_name);
	DBMS_OUTPUT.PUT_LINE(temp_sal);
	END LOOP;
	CLOSE c_name;
end;
