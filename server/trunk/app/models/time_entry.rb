class TimeEntry < ActiveRecord::Base
  belongs_to :customer
  belongs_to :time_entry_type
  belongs_to :position
end
