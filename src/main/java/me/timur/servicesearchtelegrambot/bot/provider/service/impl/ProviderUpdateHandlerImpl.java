package me.timur.servicesearchtelegrambot.bot.provider.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.timur.servicesearchtelegrambot.bot.ChatLogType;
import me.timur.servicesearchtelegrambot.bot.ConfigName;
import me.timur.servicesearchtelegrambot.bot.Region;
import me.timur.servicesearchtelegrambot.bot.provider.enums.Command;
import me.timur.servicesearchtelegrambot.bot.provider.enums.Outcome;
import me.timur.servicesearchtelegrambot.bot.provider.service.NotificationService;
import me.timur.servicesearchtelegrambot.bot.provider.service.ProviderUpdateHandler;
import me.timur.servicesearchtelegrambot.bot.util.KeyboardUtil;
import me.timur.servicesearchtelegrambot.bot.util.StringUtil;
import me.timur.servicesearchtelegrambot.enitity.*;
import me.timur.servicesearchtelegrambot.model.dto.ServiceProviderDTO;
import me.timur.servicesearchtelegrambot.repository.ProviderServiceRepository;
import me.timur.servicesearchtelegrambot.repository.ProviderServiceSubscriptionRepository;
import me.timur.servicesearchtelegrambot.service.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static me.timur.servicesearchtelegrambot.bot.util.UpdateUtil.*;

/** Created by Temurbek Ismoilov on 25/09/22. */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProviderUpdateHandlerImpl implements ProviderUpdateHandler {
  private final ProviderManager providerManager;
  private final ChatLogService chatLogService;
  private final ServiceManager serviceManager;
  private final ProviderServiceRepository providerServiceRepository;
  private final ProviderServiceSubscriptionRepository subscriptionRepository;
  private final QueryService queryService;
  private final NotificationService notificationService;
  private final ConfigService configService;

  @Value("${keyboard.size.row}")
  private Integer keyboardRowSize;

  @Value("${channel.service.searcher.id}")
  private String serviceSearchChannelId;

  @Override
  public SendMessage start(Update update) {
    // save if user doesn't exist
    Provider provider = providerManager.getOrSave(user(update));

    List<String> keyboard = new ArrayList<>();
    keyboard.add(Outcome.SKIP.getText());
    final SendMessage sendMessage =
        logAndKeyboard(
            update,
            "Добро пожаловать. Здесь вы можете опубликовать услуги, которые вы хотите предложить\n\n"
                + "Для начало просим ответить на несколько вопросов о вашей фирме/деятельности. "
                + "Ответы на вопросы необязательные и можете пропустить, если не хотите отвечать. "
                + "Но мы рекомендуем дать на больше информации так, как это увеличит доверии к вам\n\n"
                + "Напишите Ваше имя и фамилию",
            keyboard,
            1,
            Outcome.NAME_REQUESTED);
    return sendMessage;
  }

  @Override
  public SendMessage requestPhone(Update update) {
    final String newCommand = command(update);
    // validate the command
    if (containsBannedWord(newCommand)) {
      return message(chatId(update), "Запрещенное слово");
    }

    if (!Objects.equals(newCommand, Outcome.SKIP.getText())) {
      Provider provider = providerManager.getByUserTelegramId(tgUserId(update));
      provider.setName(newCommand);
      providerManager.save(provider);
    }
    final SendMessage msg =
        logAndMessage(update, "Поделитесь номером телефона", Outcome.PHONE_REQUESTED);
    msg.setReplyMarkup(KeyboardUtil.phoneRequest());
    return msg;
  }

  @Override
  public SendMessage requestCompanyInfo(Update update) {
    final Contact contact = update.getMessage().getContact();
    if (contact != null) {
      Provider provider = providerManager.getByUserTelegramId(tgUserId(update));
      provider.setPhone(contact.getPhoneNumber());
      providerManager.save(provider);
    }
    List<String> keyboard = new ArrayList<>();
    keyboard.add(Outcome.SKIP.getText());
    return logAndKeyboard(
        update,
        Outcome.COMPANY_INFO_REQUESTED.getText(),
        keyboard,
        1,
        Outcome.COMPANY_INFO_REQUESTED);
  }

  @Override
  public SendMessage requestRegion(Update update) {
    final String newCommand = command(update);
    // validate the command
    if (containsBannedWord(newCommand)) {
      return message(chatId(update), "Запрещенное слово");
    }
    if (!Objects.equals(newCommand, Outcome.SKIP.getText())) {
      Provider provider = providerManager.getByUserTelegramId(tgUserId(update));
      provider.setCompanyInformation(newCommand);
      providerManager.save(provider);
    }

    return logAndKeyboard(
        update,
        Outcome.REGION_REQUESTED.getText(),
        Arrays.stream(Region.values()).map(r -> r.russian).collect(Collectors.toList()),
        2,
        Outcome.REGION_REQUESTED);
  }

  @Override
  public SendMessage requestService(Update update) {
    return logAndKeyboard(
            update,
            Outcome.REQUEST_SERVICE_NAME.getText(),
            backButton(),
            2,
            Outcome.REQUEST_SERVICE_NAME
    );
  }

  @Override
  public SendMessage searchService(Update update) {
    String command = command(update);

    if (StringUtil.isLatin(command)) {
        return keyboard(
                chatId(update),
                "Текст должен быть на кириллице. Попробуйте ещё раз",
                List.of(Outcome.CATEGORIES.getText(), Command.BACK.getText()),
                2
        );
    }

    SendMessage sendMessage;
    final List<Service> services = serviceManager.getAllServicesByActiveTrueAndNameLike(command);
    if (services.isEmpty()) {
      List<String> keyboardValues = List.of(Outcome.CATEGORIES.getText(), Command.BACK.getText());
      sendMessage =
          logAndMessage(
              update, Outcome.SERVICE_SEARCH_NOT_FOUND.getText(), Outcome.SERVICE_SEARCH_NOT_FOUND);
      sendMessage.setReplyMarkup(keyboard(keyboardValues, keyboardRowSize));
    } else {
      final List<String> serviceNames =
          services.stream().map(Service::getNameUz).collect(Collectors.toList());
      serviceNames.add(Outcome.CATEGORIES.getText());
      serviceNames.add(Command.BACK.getText());
      sendMessage =
          logAndKeyboard(
              update,
              Outcome.SERVICE_SEARCH_FOUND.getText(),
              serviceNames,
              keyboardRowSize,
              Outcome.SERVICE_SEARCH_FOUND);
    }

    return sendMessage;
  }

  @Override
  public List<SendMessage> handleQuery(Update update) {
    String chatText = "";
    try {
      // get query id from chat text
      chatText = update.getChannelPost().getText();
      Long queryId = Long.valueOf(chatText.substring(chatText.indexOf("#") + 1));

      // get providers who can handle the query
      Query query = queryService.getById(queryId);
      List<Provider> providers =
          providerManager.findAllByServiceAndRegionAndActiveSubscription(
              query.getService(), query.getClient().getRegion());

      // prepare notifications for those providers
      List<SendMessage> messages = new ArrayList<>();
      for (Provider provider : providers) {
        String chatId = provider.getUser().getTelegramId().toString();
        List<String> keyboardTexts = new ArrayList<>();
        keyboardTexts.add(Command.ACCEPT_QUERY.getText() + queryId);
        keyboardTexts.add(Command.DENY_QUERY.getText());
        messages.add(
            keyboard(
                chatId,
                "Новый запрос #"
                    + queryId
                    + "\n"
                    + query.getService().getName()
                    + (query.getComment() != null ? "\n" + query.getComment() : ""),
                keyboardTexts,
                keyboardRowSize));
      }
      // send provider id list to channel
      final String providerIds =
          providers.stream().map(p -> String.valueOf(p.getId())).collect(Collectors.joining(", "));

      SendMessage channelReply =
          message(
              serviceSearchChannelId,
              "#"
                  + queryId
                  + " provider IDs: "
                  + (providerIds.isEmpty() ? "NOT FOUND" : providerIds));

      messages.add(channelReply);

      return messages;
    } catch (Exception e) {
      log.error("Error while handling channel post: {}", chatText);
      log.error(e.getMessage(), e);
      return new ArrayList<SendMessage>();
    }
  }

  @Override
  public List<SendMessage> acceptQuery(Update update) {
    // client clientMsg
    SendMessage clientMsg = message(chatId(update), "Можете связаться с заказчиком: ");
    clientMsg.setReplyMarkup(keyboard(mainMenu(),2));
    // prepare reply
    List<SendMessage> messages = new ArrayList<>();
    messages.add(clientMsg);

    // get query
    String command = command(update);
    Long queryId = Long.valueOf(command.substring(command.indexOf("#") + 1));
    Query query = queryService.getById(queryId);

    // check if query is still active
    if (!query.getIsActive()) {
      clientMsg.setText("Клиент закрыл запрос");
      return messages;
    }

    // fetch client and provider
    final User client = query.getClient();
    final Provider provider = providerManager.getByUserTelegramId(Long.valueOf(chatId(update)));

    // clientMsg to client
    if (client.getTelegramId() != null) {
      notificationService.sendNotification(
          client.getTelegramId().toString(), new ServiceProviderDTO(provider));
    }

    // clientMsg to provider
    if (client.getUsername() != null)
      clientMsg.setText(clientMsg.getText() + "@" + client.getUsername());
    else clientMsg.setText(clientMsg.getText() + client.getPhone());

    // clientMsg to the channel
    SendMessage channelMsg =
        message(serviceSearchChannelId, provider.getName() + " готов обработать заказ #" + queryId);
    messages.add(channelMsg);

    return messages;
  }

  @Override
  public SendMessage denyQuery(Update update) {
    return keyboard(chatId(update), "Запрос не принят", mainMenu(), 2);
  }

  @Override
  public SendMessage settings(Update update) {
    Provider provider = providerManager.getByUserTelegramId(tgUserId(update));
    List<String> keyboardValues = new ArrayList<>();

    if (provider.getCompanyName() == null)
      keyboardValues.add("➕ " + Outcome.COMPANY_NAME.getText());
    else
      keyboardValues.add("✏️ " + Outcome.COMPANY_NAME.getText() + ": " + provider.getCompanyName());

    if (provider.getCompanyAddress() == null)
      keyboardValues.add("➕ " + Outcome.COMPANY_ADDRESS_REQUESTED.getText());
    else
      keyboardValues.add(
          "✏️ "
              + Outcome.COMPANY_ADDRESS_REQUESTED.getText()
              + ": "
              + provider.getCompanyAddress());

    if (provider.getWebsite() == null)
      keyboardValues.add("➕ " + Outcome.WEBSITE_REQUESTED.getText());
    else
      keyboardValues.add(
          "✏️ " + Outcome.WEBSITE_REQUESTED.getText() + ": " + provider.getWebsite());

    if (provider.getInstagram() == null)
      keyboardValues.add("➕ " + Outcome.INSTAGRAM_REQUESTED.getText());
    else
      keyboardValues.add(
          "✏️ " + Outcome.INSTAGRAM_REQUESTED.getText() + ": " + provider.getInstagram());

    if (provider.getTelegram() == null)
      keyboardValues.add("➕ " + Outcome.TELEGRAM_REQUESTED.getText());
    else
      keyboardValues.add(
          "✏️ " + Outcome.TELEGRAM_REQUESTED.getText() + ": " + provider.getTelegram());

    if (provider.getCertificateTgFileId() == null)
      keyboardValues.add("➕ " + Outcome.CERTIFICATE_REQUESTED.getText());
    else keyboardValues.add("✏️ " + Outcome.CERTIFICATE_REQUESTED.getText() + ": Загружен ✅");

    if (provider.getCompanyInformation() == null)
      keyboardValues.add("➕ " + Outcome.COMPANY_INFO_REQUESTED.getText());
    else {
      final int length = provider.getCompanyInformation().length();
      keyboardValues.add(
          "✏️ "
              + Outcome.COMPANY_INFO_REQUESTED.getText()
              + ": "
              + provider
                  .getCompanyInformation()
                  .substring(0, (length <= 20L ? length : length / 5)));
    }

    if (provider.getRegion() == null)
      keyboardValues.add("➕ " + Outcome.REGION_EDIT_REQUESTED.getText());
    else keyboardValues.add("✏️ " + "Регион" + ": " + provider.getRegion());

    keyboardValues.add(Command.OFFER_BUTTON.getText());
    // add back button
    keyboardValues.addAll(backButton());

    return logAndKeyboard(
        update, Outcome.SETTINGS.getText(), keyboardValues, keyboardRowSize, Outcome.SETTINGS);
  }

  @Override
  public SendMessage saveRegionAndRequestService(Update update) {
    Region region = Region.getByRussian(command(update));
    if (region == null) {
      return logAndKeyboard(
          update,
          Outcome.REGION_REQUESTED.getText(),
          Arrays.stream(Region.values()).map(r -> r.russian).collect(Collectors.toList()),
          2,
          Outcome.REGION_REQUESTED);
    } else {
      // set region for a provide
      Provider provider = providerManager.getByUserTelegramId(Long.valueOf(chatId(update)));
      provider.setRegion(region);
      providerManager.save(provider);
      // request service name
      return requestService(update);
    }
  }

  @Override
  public SendMessage editRegion(Update update) {
    return logAndKeyboard(
        update,
        Outcome.REGION_REQUESTED.getText(),
        Arrays.stream(Region.values()).map(r -> r.russian).collect(Collectors.toList()),
        2,
        Outcome.REGION_EDIT_REQUESTED);
  }

  @Override
  public SendMessage saveRegion(Update update) {
    Region region = Region.getByRussian(command(update));
    Provider provider = providerManager.getByUserTelegramId(Long.valueOf(chatId(update)));
    provider.setRegion(region);
    providerManager.save(provider);
    return settings(update);
  }

  @Override
  public SendMessage editCompanyName(Update update) {
    List<String> keyboard = new ArrayList<>();
    keyboard.add(Outcome.BACK.getText());
    return logAndKeyboard(
        update,
        "Напишите " + Outcome.COMPANY_NAME.getText().toLowerCase(Locale.ROOT),
        keyboard,
        1,
        Outcome.COMPANY_NAME);
  }

  @Override
  public SendMessage saveCompanyName(Update update) {
    final String newCommand = command(update);
    // validate the command
    if (containsBannedWord(newCommand)) {
      return message(chatId(update), "Запрещенное слово");
    }

    Provider provider = providerManager.getByUserTelegramId(tgUserId(update));
    provider.setCompanyName(newCommand);
    providerManager.save(provider);
    return settings(update);
  }

  @Override
  public SendMessage editCompanyAddress(Update update) {
    List<String> keyboard = new ArrayList<>();
    keyboard.add(Outcome.BACK.getText());
    return logAndKeyboard(
        update,
        "Напишите " + Outcome.COMPANY_ADDRESS_REQUESTED.getText().toLowerCase(Locale.ROOT),
        keyboard,
        1,
        Outcome.COMPANY_ADDRESS_REQUESTED);
  }

  @Override
  public SendMessage saveCompanyAddress(Update update) {
    final String newCommand = command(update);
    // validate the command
    if (containsBannedWord(newCommand)) {
      return message(chatId(update), "Запрещенное слово");
    }
    Provider provider = providerManager.getByUserTelegramId(tgUserId(update));
    provider.setCompanyAddress(newCommand);
    providerManager.save(provider);
    return settings(update);
  }

  @Override
  public SendMessage editWebsite(Update update) {
    List<String> keyboard = new ArrayList<>();
    keyboard.add(Outcome.BACK.getText());
    return logAndKeyboard(
        update,
        "Напишите " + Outcome.WEBSITE_REQUESTED.getText().toLowerCase(Locale.ROOT),
        keyboard,
        1,
        Outcome.WEBSITE_REQUESTED);
  }

  @Override
  public SendMessage saveWebsite(Update update) {
    final String newCommand = command(update);
    // validate the command
    if (containsBannedWord(newCommand)) {
      return message(chatId(update), "Запрещенное слово");
    }
    Provider provider = providerManager.getByUserTelegramId(tgUserId(update));
    provider.setWebsite(newCommand);
    providerManager.save(provider);
    return settings(update);
  }

  @Override
  public SendMessage editInstagram(Update update) {
    List<String> keyboard = new ArrayList<>();
    keyboard.add(Outcome.BACK.getText());
    return logAndKeyboard(
        update,
        "Напишите " + Outcome.INSTAGRAM_REQUESTED.getText().toLowerCase(Locale.ROOT),
        keyboard,
        1,
        Outcome.INSTAGRAM_REQUESTED);
  }

  @Override
  public SendMessage saveInstagram(Update update) {
    final String newCommand = command(update);
    // validate the command
    if (containsBannedWord(newCommand)) {
      return message(chatId(update), "Запрещенное слово");
    }
    Provider provider = providerManager.getByUserTelegramId(tgUserId(update));
    provider.setInstagram(newCommand);
    providerManager.save(provider);
    return settings(update);
  }

  @Override
  public SendMessage editCompanyInfo(Update update) {
    List<String> keyboard = new ArrayList<>();
    keyboard.add(Outcome.BACK.getText());
    return logAndKeyboard(
        update,
        "Напишите " + Outcome.COMPANY_INFO_REQUESTED.getText().toLowerCase(Locale.ROOT),
        keyboard,
        1,
        Outcome.COMPANY_INFO_REQUESTED);
  }

  @Override
  public SendMessage saveCompanyInfo(Update update) {
    final String newCommand = command(update);
    // validate the command
    if (containsBannedWord(newCommand)) {
      return message(chatId(update), "Запрещенное слово");
    }
    Provider provider = providerManager.getByUserTelegramId(tgUserId(update));
    provider.setCompanyInformation(newCommand);
    providerManager.save(provider);
    return settings(update);
  }

  @Override
  public SendMessage editTelegram(Update update) {
    List<String> keyboard = new ArrayList<>();
    keyboard.add(Outcome.BACK.getText());
    return logAndKeyboard(
        update,
        "Напишите " + Outcome.TELEGRAM_REQUESTED.getText().toLowerCase(Locale.ROOT),
        keyboard,
        1,
        Outcome.TELEGRAM_REQUESTED);
  }

  @Override
  public SendMessage saveTelegram(Update update) {
    final String newCommand = command(update);
    // validate the command
    if (containsBannedWord(newCommand)) {
      return message(chatId(update), "Запрещенное слово");
    }
    Provider provider = providerManager.getByUserTelegramId(tgUserId(update));
    provider.setTelegram(newCommand);
    providerManager.save(provider);
    return settings(update);
  }

  @Override
  public SendMessage editCertificate(Update update) {
    List<String> keyboard = new ArrayList<>();
    keyboard.add(Outcome.BACK.getText());
    return logAndKeyboard(
        update,
        Outcome.CERTIFICATE_REQUESTED.getText().toLowerCase(Locale.ROOT),
        keyboard,
        1,
        Outcome.CERTIFICATE_REQUESTED);
  }

  @Override
  public SendMessage saveCertificate(Update update) {
    Provider provider = providerManager.getByUserTelegramId(tgUserId(update));
    final Document document = update.getMessage().getDocument();
    provider.setCertificateTgFileId(document.getFileId());
    provider.setCertificateMyType(FilenameUtils.getExtension(document.getFileName()));
    providerManager.save(provider);
    return settings(update);
  }

  @Override
  public SendMessage getMyServices(Update update) {
    List<String> servicesNames =
        providerServiceRepository.findAllByProviderUserTelegramId(tgUserId(update)).stream()
                .sorted((o1, o2) -> Boolean.compare(o2.getActive(), o1.getActive()))
                .map(ps -> (ps.getActive() ? "\uD83D\uDFE2 " : "\uD83D\uDD34 ") + ps.getService().getName())
                .collect(Collectors.toList());
    servicesNames.addAll(backButton());
    String text =
        servicesNames.size() != 0 ? Outcome.MY_SERVICES.getText() : "У вас нет активных услуг";
    return logAndKeyboard(update, text, servicesNames, keyboardRowSize, Outcome.MY_SERVICES);
  }

  @Override
  public SendMessage editProviderService(Update update) {
    String command = command(update);
    String serviceName = command.substring(command.indexOf(" ") + 1);
    Optional<ProviderService> serviceOpt =
        providerServiceRepository.findByProviderAndService(
            providerManager.getByUserTelegramId(tgUserId(update)),
            serviceManager.getServiceByName(serviceName));

    if (serviceOpt.isPresent()) {
      ProviderService service = serviceOpt.get();
      String text = Outcome.SERVICE_EDIT_REQUESTED.getText();
      String button =
          (service.getActive()
                  ? Command.UNSUBSCRIBE_FROM_SERVICE.getText()
                  : Command.SUBSCRIBE_TO_SERVICE.getText())
              + " "
              + serviceName;
      List<String> buttons = new ArrayList<>();
      buttons.add(button);
      buttons.add(Outcome.BACK.getText());
      return logAndKeyboard(update, text, buttons, 1, Outcome.SERVICE_EDIT_REQUESTED);
    }

    return keyboard(chatId(update), Outcome.MAIN_MENU.getText(), mainMenu(), 2);
  }

  @Override
  public SendMessage unsubscribeFromService(Update update) {
    // get service
    String serviceName =
        command(update).substring(Command.UNSUBSCRIBE_FROM_SERVICE.getText().length() + 1);
    Optional<ProviderService> serviceOpt =
        providerServiceRepository.findByProviderAndService(
            providerManager.getByUserTelegramId(tgUserId(update)),
            serviceManager.getServiceByName(serviceName));

    if (serviceOpt.isPresent()) {
      // update service
      ProviderService service = serviceOpt.get();
      service.setActive(false);
      providerServiceRepository.save(service);
      // send reply
      return logAndKeyboard(
              update,
              Outcome.SERVICE_UNSUBSCRIBED.getText(),
              mainMenu(),
              2,
              Outcome.SERVICE_UNSUBSCRIBED);
    }

    return mainMenu(update);
  }

  @Override
  public SendMessage subscribeToService(Update update) {
    // get service
    String serviceName =
        command(update).substring(Command.SUBSCRIBE_TO_SERVICE.getText().length() + 1);
    Optional<ProviderService> serviceOpt =
        providerServiceRepository.findByProviderAndService(
            providerManager.getByUserTelegramId(tgUserId(update)),
            serviceManager.getServiceByName(serviceName));

    if (serviceOpt.isPresent()) {
      // update service
      ProviderService service = serviceOpt.get();
      service.setActive(true);
      providerServiceRepository.save(service);
      // send reply
      return logAndKeyboard(update, Outcome.SERVICE_SUBSCRIBED.getText(), mainMenu(), 2, Outcome.SERVICE_SUBSCRIBED);
    }

    return keyboard(chatId(update), Outcome.MAIN_MENU.getText(), mainMenu(), 2);
  }

  @Override
  public SendMessage getQueries(Update update) {
    final Provider provider = providerManager.getByUserTelegramId(Long.valueOf(chatId(update)));
    final List<Service> services =
        providerServiceRepository
            .findAllByProviderUserTelegramId(Long.valueOf(chatId(update)))
            .stream()
            .map(ProviderService::getService)
            .collect(Collectors.toList());

    List<String> queries =
        queryService.getAllByServicesAndRegion(services, provider.getRegion()).stream()
            .map(
                q ->
                    q.getService().getName()
                        + (q.getComment() != null ? " / " + q.getComment() : "")
                        + " / Заказ #"
                        + q.getId())
            .collect(Collectors.toList());

    if (queries.isEmpty()) {
      return logAndKeyboard(
          update, Outcome.QUERY_NOT_FOUND.getText(), backButton(), 2, Outcome.QUERY_NOT_FOUND);
    } else {
      queries.addAll(backButton());
      return logAndKeyboard(
          update, Outcome.QUERY_FOUND.getText(), queries, keyboardRowSize, Outcome.QUERY_FOUND);
    }
  }

  @Override
  public SendMessage publicOffer(Update update) {
    Config config = configService.getByName(ConfigName.OFFER);
    return logAndKeyboard(update, config.getValue(), backButton(), 2, Outcome.OFFER);
  }

  @Override
  public SendMessage getServicesByCategoryName(Update update) {
    List<String> servicesNames = serviceManager.getServicesNamesByCategoryName(command(update));
    ArrayList<String> modifiableList = new ArrayList<>(servicesNames);
    modifiableList.add(Command.BACK_TO_CATEGORIES.getText());
    return logAndKeyboard(
        update, command(update), modifiableList, keyboardRowSize, Outcome.SERVICES);
  }

  @Override
  public SendMessage getCategories(Update update) {
    final List<String> categoryNames = serviceManager.getActiveCategoryNames();
    categoryNames.addAll(backButton());
    return logAndKeyboard(
        update, Outcome.CATEGORIES.getText(), categoryNames, keyboardRowSize, Outcome.CATEGORIES);
  }

  @Override
  public SendMessage saveServiceIfServiceFoundOrSearchFurther(Update update) {
    SendMessage sendMessage = null;
    Service service = serviceManager.getServiceByName(command(update));
    Provider provider = providerManager.getByUserTelegramId(tgUserId(update));

    // check if it's already registered
    Optional<ProviderService> providerServiceOpt =
        providerServiceRepository.findByProviderAndService(provider, service);

    if (providerServiceOpt.isPresent()) {
      sendMessage =
          logAndKeyboard(
              update,
              Outcome.PROVIDER_SERVICE_ALREADY_EXISTS.getText(),
              mainMenu(),
              2,
              Outcome.PROVIDER_SERVICE_ALREADY_EXISTS);
    } else {
      final ProviderService providerService =
          providerServiceRepository.save(new ProviderService(provider, service, true));
      subscriptionRepository.save(
          new ProviderServiceSubscription(
              providerService, LocalDate.now(), LocalDate.now().plusDays(3)));
      sendMessage =
          logAndKeyboard(
              update,
              Outcome.PROVIDER_SERVICE_SAVED.getText(),
              mainMenu(),
              2,
              Outcome.PROVIDER_SERVICE_SAVED);
    }
    return sendMessage;
  }

  @Override
  public SendMessage backButton(Update update) {
    String lastChatCommand = chatLogService.getLastChatOutcome(update, ChatLogType.PROVIDER);
    if (List.of(Outcome.SERVICE_SEARCH_FOUND.name(), Outcome.SERVICE_SEARCH_NOT_FOUND.name())
        .contains(lastChatCommand)) {
      return requestService(update);
    } else if (Objects.equals(lastChatCommand, Outcome.SETTINGS.name())) {
      return mainMenu(update);
    } else if (Objects.equals(lastChatCommand, Outcome.GET_QUERIES.name())) {
      return mainMenu(update);
    } else if (Objects.equals(lastChatCommand, Outcome.CATEGORIES.name())) {
      return requestService(update);
    } else if (Objects.equals(lastChatCommand, Outcome.MY_SERVICES.name())) {
      return mainMenu(update);
    } else if (Objects.equals(lastChatCommand, Outcome.OFFER.name())) {
      return settings(update);
    } else if (Objects.equals(lastChatCommand, Outcome.REQUEST_SERVICE_NAME.name())) {
      return mainMenu(update);
    } else {
      return mainMenu(update);
    }
  }

  private SendMessage mainMenu(Update update) {
    return logAndKeyboard(update, Outcome.MAIN_MENU.getText(), mainMenu(), 2, Outcome.BACK);
  }

  @Override
  public SendMessage unknownCommand(Update update) {
    return logAndMessage(update, Outcome.UNKNOWN_COMMAND.getText(), Outcome.UNKNOWN_COMMAND);
  }

  public boolean containsBannedWord(String command) {
    return configService.containsBannedWord(command);
  }

  private List<String> mainMenu() {
    return List.of(
        Command.NEW_SERVICE_BUTTON.getText(),
        Command.MY_SERVICES_BUTTON.getText(),
        Command.SETTINGS_BUTTON.getText(),
        Command.GET_QUERIES_BUTTON.getText());
  }

  private List<String> backButton() {
    return List.of(Command.BACK.getText());
  }

  private SendMessage logAndMessage(Update update, String message, Outcome outcome) {
    chatLogService.log(update, outcome, ChatLogType.PROVIDER);
    return message(chatId(update), message);
  }

  private SendMessage logAndKeyboard(
      Update update,
      String message,
      List<String> serviceNames,
      Integer keyboardRowSize,
      Outcome outcome) {
    chatLogService.log(update, outcome, ChatLogType.PROVIDER);
    return keyboard(chatId(update), message, serviceNames, keyboardRowSize);
  }
}
