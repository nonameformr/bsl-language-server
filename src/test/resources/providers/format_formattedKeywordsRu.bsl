Асинх Процедура тест(Знач парам1) Экспорт
  Перем тест;
  
  Если условие И УсловиеДва Или Не Условие3 Тогда
  ИначеЕсли Условие4 Тогда
  Иначе
  КонецЕсли;
  
  Пока условие Цикл
    Прервать;
  КонецЦикла;
  
  Для Каждого строка Из таблица Цикл
    Продолжить;
  КонецЦикла;
  
  Попытка
    объект = Новый Массив;
  Исключение
    ВызватьИсключение "исключение";
  КонецПопытки
  
  Перейти ~Метка1;
  ~Метка1 : сообщить("привет");
  
  ДобавитьОбработчик Накладная.ПриЗаписи, Обработка.ПриЗаписиДокумента;
  УдалитьОбработчик Накладная.ПриЗаписи, Обработка.ПриЗаписиДокумента;
  
  Ждать вызов();
КонецПроцедуры

Функция тест2()
КонецФункции