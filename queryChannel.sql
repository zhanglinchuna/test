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
		
		select e.sal into temp_sal	from emp e where e.ENAME = temp_name;
		
		EXIT when c_name%NOTFOUND; -- loop循环结束条件
	DBMS_OUTPUT.PUT_LINE(temp_name);
	DBMS_OUTPUT.PUT_LINE(temp_sal);
  
	END LOOP;
	CLOSE c_name;
end;
