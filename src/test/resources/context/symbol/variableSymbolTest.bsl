
// Описание сверху
Перем ПеременнаяСОписаниемСверху;

// Описание сверху
// Еще немного описания
Перем ПеременнаяСОписаниемСверху2;

Перем ПеременнаяСОписаниемВСтроке; // Описание в строке

// Неверное описание

Перем ПеременнаяСНевернымОписанием;

Перем ПеременнаяБезОписания;

Перем ПеременнаяА, ПеременнаяБ;

&НаКлиенте
Перем ПеременнаяАф; // висячий комментарий

// Комментарий сверху
&НаКлиенте
// Комментарий под аннотацией
Перем ПеременнаяАя; // висячий комментарий

// Это описание метода
//
// Параметры:
//  ПараметрПроцедуры     - Произвольный            - Описание параметра ПараметрПроцедуры;
//  ПараметрПроцедурыЗнач - Структура, Неопределено - Описание параметра ПараметрПроцедурыЗнач.
//
Процедура А(ПараметрПроцедуры, Знач ПараметрПроцедурыЗнач = Неопределенно)
    Перем Локальная;

    Перем Локальнаяв;// висячий комментарий

    // комментарий сверху
    &НаКлиенте
    Перем Локальнаяя;// висячий комментарий

    // комментарий сверху
    // комментарий сверху 2
    &НаКлиенте
    // комментарий сверху 3
    // комментарий сверху 4
    Перем Локальнаяяя;

    ПеременнаяСоздаваемаяВКоде = Неопределенно; // Произвольный
    ПеременнаяСоздаваемаяВКодеВторойПример = МойМетод(); // Число
    ПараметрПроцедуры = 1;
    А.ЭтоНеПеременная = Неопределенно;

КонецПроцедуры

ПеременнаяСоздаваемаяВКодеВнеМетода = Неопределенно;
ПеременнаяСоздаваемаяВКодеВнеМетодаВторойПример = МойМетод();
А.ЭтоНеПеременная = Неопределенно;

Для Каждого МетодБезКоментария Из Методы Цикл
    МойМетод(Метод);
КонецЦикла;

Для Каждого МетодСКоментарием Из Методы Цикл // Висячий комментарий для цикла
    МойМетод(Метод);
КонецЦикла;

Для СчДляБезКоментария = 1 По 100 Цикл
    МойМетод(Метод);
КонецЦикла;

Для СчДляСКоментарием = 1 По 100 Цикл // Висячий комментарий для цикла
    МойМетод(Метод);
КонецЦикла;