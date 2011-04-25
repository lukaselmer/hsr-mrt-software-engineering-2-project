class TimeEntryTypeMaterial < ActiveRecord::Base
  belongs_to :time_entry_type
  belongs_to :material
end
