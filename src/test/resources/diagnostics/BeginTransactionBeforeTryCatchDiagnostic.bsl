Процедура Пример1() // правильнй с ИТС
    НачатьТранзакцию();
    Попытка
        БлокировкаДанных = Новый БлокировкаДанных;
        ЭлементБлокировкиДанных = БлокировкаДанных.Добавить("Документ.ПриходнаяНакладная");
        ЭлементБлокировкиДанных.УстановитьЗначение("Ссылка", СсылкаДляОбработки);
        ЭлементБлокировкиДанных.Режим = РежимБлокировкиДанных.Исключительный;
        БлокировкаДанных.Заблокировать();

        ДокументОбъект.Записать();

        ЗафиксироватьТранзакцию();
    Исключение
        ОтменитьТранзакцию();

        ЗаписьЖурналаРегистрации(НСтр("ru = 'Выполнение операции'"),
            УровеньЖурналаРегистрации.Ошибка,
            ,
            ,
            ПодробноеПредставлениеОшибки(ИнформацияОбОшибке()));

        ВызватьИсключение; // есть внешняя транзакция

    КонецПопытки;
КонецПроцедуры

// ошибочные конструкции

Процедура Пример2()
    НачатьТранзакцию(); // <-- Ошибка: код перед попыткой
    Метод();
    Попытка
        Метод2();
    Исключение
        ОтменитьТранзакцию();
        Возврат;
    КонецПопытки;
    ЗафиксироватьТранзакцию();
КонецПроцедуры

Процедура Пример3()
    Попытка
        НачатьТранзакцию(); // <-- Ошибка: в попытке
        Метод();
    Исключение
        Если ТранзакцияАктивна() Тогда
            ЗафиксироватьТранзакцию();
        Иначе
            ОтменитьТранзакцию();
        КонецЕсли;
        Возврат;
    КонецПопытки;
КонецПроцедуры

Процедура Пример4()
    НачатьТранзакцию(); // <-- Ошибка: код после начала
    Метод();
    Если ТранзакцияАктивна() Тогда
        ЗафиксироватьТранзакцию();
    Иначе
        ОтменитьТранзакцию();
    КонецЕсли;
КонецПроцедуры

Функция Пример5()
    НачатьТранзакцию(); // <-- Ошибки нет
    Попытка
        Метод();
        НачатьТранзакцию(); // <-- Ошибка: есть код после
        Метод2();
        ЗафиксироватьТранзакцию();
    Исключение
    КонецПопытки;
    Возврат 1;
КонецФункции

Процедура Пример6()
    НачатьТранзакцию(); // <-- Ошибка: есть код после
    НачатьТранзакцию(); // <-- Ошибки нет
    Попытка
        Метод();
        ЗафиксироватьТранзакцию();
        Метод2();
    Исключение
        ОтменитьТранзакцию();
        Возврат;
    КонецПопытки;
КонецПроцедуры

Для каждого Элемент Из Коллекция Цикл
    BeginTransaction();  // <-- Ошибка: есть код после
    Метод();
    Попытка
        Метод();
        ЗафиксироватьТранзакцию();
        Продолжить;
    Исключение
        ОтменитьТранзакцию();
        Возврат;
    КонецПопытки;
КонецЦикла;

НачатьТранзакцию(); // <-- Ошибка: просто в конце модуля

Коннектор.НачатьТранзакцию(); // Игнорируем, это не ошибка
ОбработкаПотокаНачатьТранзакцию(); // Игнор
BeginTransactionОбработкаПотокаНачать(); // Игнор
ОбработкаBeginTransactionПотокаНачать(); // Игнор
