Rails.application.config.middleware.use ExceptionNotifier,
  :email_prefix => "[MRT Exception Notification] ",
  :sender_address => %{"MRT Exception Notifier" <mrt@elmermx.ch>},
  :exception_recipients => %w{lukas.elmer@gmail.com}
