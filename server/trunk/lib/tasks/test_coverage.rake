require 'rcov/rcovtask'
#require 'task_helpers'
#require 'flog'

desc 'Run all Rails Code QA tests'
task :tc => ["test_coverage:default"]
task :test_coverage => ["test_coverage:default"]


namespace :test_coverage do
  SECTIONS = {
    "unit" => {:folders => "app\/models|app\/mailers"},
    #"functionals" => {:folders => "app\/controllers|app\/mailers"},
    "functional" => {:folders => "app\/models|app\/helpers|lib|app\/controllers|app\/mailers|app\/views"},
    #"integration" => {}
  }

  task :default => [:test] #, :flog, :flay]

  desc "Run functional Rails tests with rcov on units and functionals"
  task(:test) do
    SECTIONS.each do |section_name, section|
      puts "#################################################"
      puts "Running #{section_name} tests..."
      puts "#################################################"
      Rake::Task["test_coverage:#{section_name}"].invoke
      unless section[:folders].nil?
        file_location = "file:///#{File.join([Rails.root, 'doc', 'coverage', section_name, 'index.html'])}"
        puts "HTML output: <#{file_location}>"
        puts "Opening brwoser..."
        system("#{ENV['OS'] == 'Windows_NT' ? "explorer" : "x-www-browser"} #{file_location}")
      end
      puts "\n\n"
    end
  end

  Rcov::RcovTask.new("functional") do |t|
    t.libs << "test"
    t.test_files = Dir["test/**/*_test.rb"]
    include_folders = "app\/models|app\/helpers|lib|app\/controllers|app\/mailers|app\/views"
    t.rcov_opts = ["--html", "--text-report", "--exclude \"^(?!(#{include_folders}))\""]
    t.output_dir = "doc/coverage/functional"
  end
  
  Rcov::RcovTask.new("unit") do |t|
    t.libs << "test"
    t.test_files = Dir["test/unit/*_test.rb"]
    include_folders = SECTIONS['unit'][:folders]
    t.rcov_opts = ["--html", "--text-report", "--exclude \"^(?!(#{include_folders}))\""]
    t.output_dir = "doc/coverage/unit"
  end

  #task :integration => ["test:integration"]

  #  desc "Run Flog"
  #  task(:flog) do
  #    flog_runner(30, ['app/models', 'app/helpers', 'lib'])
  #    flog_runner(45, ['app/controllers'])
  #  end

  #  desc "Run Flay"
  #  task(:flay) do
  #    require 'flay'
  #    puts "=============================================="
  #    puts "Flay output: "
  #    threshold = 25
  #    flay = Flay.new({:fuzzy => false, :verbose => false, :mass => threshold})
  #    flay.process(*Flay.expand_dirs_to_files(['app/models', 'app/helpers', 'lib']))
  #    flay.report
  #
  #    puts "#{flay.masses.size} chunks of code have a duplicate mass > #{threshold}" unless flay.masses.empty?
  #    puts "=============================================="
  #    puts ""
  #  end

end



